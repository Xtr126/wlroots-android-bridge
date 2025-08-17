import com.android.build.gradle.internal.BuildToolsExecutableInput
import com.android.build.gradle.internal.LoggerWrapper
import com.android.build.gradle.internal.component.ConsumableCreationConfig
import com.android.build.gradle.internal.initialize
import com.android.build.gradle.internal.process.GradleProcessExecutor
import com.android.build.gradle.internal.profile.ProfileAwareWorkAction
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactScope.ALL
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ArtifactType.AIDL
import com.android.build.gradle.internal.publishing.AndroidArtifacts.ConsumedConfigType.COMPILE_CLASSPATH
import com.android.build.gradle.internal.tasks.AndroidVariantTask
import com.android.build.gradle.internal.tasks.BuildAnalyzer
import com.android.build.gradle.internal.tasks.NonIncrementalTask
import com.android.build.gradle.internal.tasks.factory.VariantTaskCreationAction
import com.android.build.gradle.internal.utils.setDisallowChanges
import com.android.build.gradle.tasks.AidlCompile
import com.android.buildanalyzer.common.TaskCategory
import com.android.builder.compiling.DependencyFileProcessor
import com.android.builder.internal.compiler.DirectoryWalker
import com.android.builder.internal.compiler.DirectoryWalker.FileAction
import com.android.builder.internal.incremental.DependencyData
import com.android.ide.common.process.LoggedProcessOutputHandler
import com.android.ide.common.process.ProcessException
import com.android.ide.common.process.ProcessExecutor
import com.android.ide.common.process.ProcessInfoBuilder
import com.android.ide.common.process.ProcessOutputHandler
import com.android.repository.io.FileOpUtils
import com.android.utils.FileUtils
import com.google.common.io.Files
import java.io.IOException
import java.io.Serializable
import java.nio.file.Path
import java.util.Collections
import kotlin.io.path.pathString
import kotlin.jvm.java

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.xtr.tinywl"
    compileSdk = 36

    defaultConfig {
        minSdk = 33

        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                arguments.add(
                    "-Dsdk_optional_libbinder_ndk_cpp=${sdkDirectory.absolutePath}/platforms/android-${compileSdk}/optional/libbinder_ndk_cpp",
                )
                abiFilters("x86_64")
            }
        }
    }

    buildTypes {
        forEach {
            it.externalNativeBuild.cmake.arguments.add(
                "-Daidl_source_output_dir=${layout.buildDirectory.get().asFile.absolutePath}/generated/aidl_source_output_dir/${it.name}/out"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures.aidl = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    ndkVersion = "28.0.13004108"
}

dependencies {
    implementation(project(":TermuxAm"))
}
//tasks.register("compileAidlNdk", AidlCompileNdk::class)


afterEvaluate {
    tasks.named { it.contains("cmake", true) }.configureEach {
        dependsOn(tasks.withType<AidlCompile>())
    }
    tasks.withType<AidlCompile>().configureEach {

        doLast {
            // do java AIDL task first
            taskAction()

            // this is full run, clean the previous output'
            val aidlExecutable = buildTools
                .aidlExecutableProvider()
                .get()
                .absoluteFile
            val frameworkLocation = getAidlFrameworkProvider().get().absoluteFile
            val destinationDir = sourceOutputDir.get().asFile

            val parcelableDir = packagedDir.orNull
            FileUtils.cleanOutputDir(destinationDir)
            if (parcelableDir != null) {
                FileUtils.cleanOutputDir(parcelableDir.asFile)
            }

            // De-duplicate source directories- this is needed in case they are added twice like in
            // b/317262738
            val sourceFolder = project.projectDir.resolve("src/main/cpp/aidl")
            val importFolders = importDirs.files

            val fullImportList = listOf(sourceFolder) + importFolders

            AidlCompileNdk.aidlCompileDelegate(
                workerExecutor,
                aidlExecutable,
                frameworkLocation,
                destinationDir,
                parcelableDir?.asFile,
                packagedList,
                listOf(sourceFolder),
                fullImportList,
                this as AndroidVariantTask
            )
        }
    }
}



/**
 * Aidl compile task from android gradle plugin adapted for NDK
 */

/**
 * Task to compile aidl files. Supports incremental update.
 */
@CacheableTask
@BuildAnalyzer(primaryTaskCategory = TaskCategory.AIDL, secondaryTaskCategories = [TaskCategory.COMPILATION])
abstract class AidlCompileNdk : NonIncrementalTask() {
    @get:Input
    @get:Optional
    var packagedList: Collection<String>? = null
        private set

    @get:Internal
    abstract val sourceDirs: ListProperty<Directory>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    lateinit var importDirs: FileCollection
        private set

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    fun getAidlFrameworkProvider(): Provider<File> =
        buildTools.aidlFrameworkProvider()

    @get:InputFiles
    @get:IgnoreEmptyDirectories
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFiles: Property<FileTree>

    @get:OutputDirectory
    abstract val sourceOutputDir: DirectoryProperty

    @get:OutputDirectory
    @get:Optional
    abstract val packagedDir: DirectoryProperty

    @get:Nested
    abstract val buildTools: BuildToolsExecutableInput

    class DepFileProcessor : DependencyFileProcessor {
        override fun processFile(dependencyFile: File): DependencyData? {
            return DependencyData.parseDependencyFile(dependencyFile)
        }
    }

    override fun doTaskAction() {
        // this is full run, clean the previous output'
        val aidlExecutable = buildTools
            .aidlExecutableProvider()
            .get()
            .absoluteFile
        val frameworkLocation = getAidlFrameworkProvider().get().absoluteFile
        val destinationDir = sourceOutputDir.get().asFile
        val parcelableDir = packagedDir.orNull
        FileUtils.cleanOutputDir(destinationDir)
        if (parcelableDir != null) {
            FileUtils.cleanOutputDir(parcelableDir.asFile)
        }

        // De-duplicate source directories- this is needed in case they are added twice like in
        // b/317262738
        val sourceFolder = project.projectDir.resolve("src/main/cpp/aidl")
        val importFolders = importDirs.files

        val fullImportList = listOf(sourceFolder) + importFolders

        aidlCompileDelegate(
            workerExecutor,
            aidlExecutable,
            frameworkLocation,
            destinationDir,
            parcelableDir?.asFile,
            packagedList,
            listOf(sourceFolder),
            fullImportList,
            this
        )
    }

    class CreationAction(
        creationConfig: ConsumableCreationConfig
    ) : VariantTaskCreationAction<AidlCompileNdk, ConsumableCreationConfig>(
        creationConfig
    ) {

        override val name: String = computeTaskName("compile", "Aidl")

        override val type: Class<AidlCompileNdk> = AidlCompileNdk::class.java


        override fun configure(
            task: AidlCompileNdk
        ) {
            super.configure(task)
            val services = creationConfig.services

            creationConfig.sources.aidl {
                task.sourceDirs.setDisallowChanges(it.all)
                // This is because aidl may be in the same folder as Java and we want to restrict to
                // .aidl files and not java files.
                task.sourceFiles.setDisallowChanges(services.fileCollection(task.sourceDirs).asFileTree.matching(PATTERN_SET))
            }

            task.importDirs = creationConfig.variantDependencies.getArtifactFileCollection(COMPILE_CLASSPATH, ALL, AIDL)

            if (creationConfig.componentType.isAar) {
                task.packagedList = creationConfig.global.aidlPackagedList
            }
            task.buildTools.initialize(task, creationConfig)
        }
    }

    internal class ProcessingRequest(val root: File, val file: File) : Serializable

    abstract class AidlCompileRunnable : ProfileAwareWorkAction<AidlCompileRunnable.Params>() {

        abstract class Params: Parameters() {
            abstract val aidlExecutable: RegularFileProperty
            abstract val frameworkLocation: DirectoryProperty
            abstract val importFolders: ConfigurableFileCollection
            abstract val sourceOutputDir: DirectoryProperty
            abstract val packagedOutputDir: DirectoryProperty
            abstract val packagedList: ListProperty<String>
            abstract val dir: Property<File>
        }

        @get:Inject
        abstract val execOperations: ExecOperations

        override fun run() {
            // Collect all aidl files in the directory then process them
            val processingRequests = mutableListOf<ProcessingRequest>()

            val collector =
                FileAction { root: Path, file: Path ->
                    processingRequests.add(ProcessingRequest(root.toFile(), file.toFile()))
                }

            try {
                DirectoryWalker.builder()
                    .root(parameters.dir.get().toPath())
                    .extensions("aidl")
                    .filter({
                        it.pathString.contains("Surface.aidl")
                    })
                    .action(collector)
                    .build()
                    .walk()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            val depFileProcessor = DepFileProcessor()
            val executor = GradleProcessExecutor(execOperations::exec)
            val logger = LoggedProcessOutputHandler(
                LoggerWrapper.getLogger(AidlCompileRunnable::class.java))

            for (request in processingRequests) {
                AidlProcessorNdk.call(
                    parameters.aidlExecutable.get().asFile.canonicalPath,
                    parameters.frameworkLocation.get().asFile.canonicalPath,
                    parameters.importFolders.asIterable(),
                    parameters.sourceOutputDir.get().asFile,
                    parameters.packagedOutputDir.orNull?.asFile,
                    parameters.packagedList.orNull,
                    depFileProcessor,
                    executor,
                    logger,
                    request.root.toPath(),
                    request.file.toPath()
                )
            }
        }
    }

    companion object {
        private val PATTERN_SET = PatternSet().include("**/*.aidl")
        fun aidlCompileDelegate(
            workerExecutor: WorkerExecutor,
            aidlExecutable: File,
            frameworkLocation: File,
            destinationDir: File,
            parcelableDir: File?,
            packagedList: Collection<String>?,
            sourceFolders: Collection<File>,
            fullImportList: Collection<File>,
            instantiator: AndroidVariantTask
        ) {
            for (dir in sourceFolders) {
                workerExecutor.noIsolation().submit(AidlCompileRunnable::class.java) {
                    this.initializeFromBaseTask(instantiator)
                    this.aidlExecutable.set(aidlExecutable)
                    this.frameworkLocation.set(frameworkLocation)
                    this.importFolders.from(fullImportList)
                    this.sourceOutputDir.set(destinationDir)
                    this.packagedOutputDir.set(parcelableDir)
                    this.packagedList.set(packagedList)
                    this.dir.set(dir)
                }
            }
        }

    }
}

/**
 * A Source File processor for AIDL files. This compiles each aidl file found by the SourceSearcher.
 */
object AidlProcessorNdk {
    @Throws(IOException::class)
    fun call(
        aidlExecutable: String,
        frameworkLocation: String,
        importFolders: Iterable<File>,
        sourceOutputDir: File,
        packagedOutputDir: File?,
        packagedList: MutableCollection<String?>?,
        dependencyFileProcessor: DependencyFileProcessor,
        processExecutor: ProcessExecutor,
        processOutputHandler: ProcessOutputHandler,
        startDir: Path,
        inputFilePath: Path
    ) {
        val nonNullPackagedList: Set<String?> = if (packagedList == null) {
            emptySet()
        } else {
            Collections.unmodifiableSet(HashSet(packagedList))
        }

        val builder = ProcessInfoBuilder()

        builder.setExecutable(aidlExecutable)

        builder.addArgs("--lang=ndk")
        //builder.addArgs("-p$frameworkLocation")
        builder.addArgs("-o${sourceOutputDir.absolutePath}")
        builder.addArgs("-h${sourceOutputDir.absolutePath}")


        // add all the library aidl folders to access parcelables that are in libraries
        for (f in importFolders) {
            builder.addArgs("-I" + f.absolutePath)
        }

        // create a temp file for the dependency
        val depFile: File = File.createTempFile("aidl", ".d")
        builder.addArgs("-d" + depFile.absolutePath)

        builder.addArgs(inputFilePath.toAbsolutePath().toString())

        val result =
            processExecutor.execute(builder.createProcess(), processOutputHandler)

        try {
            result.rethrowFailure().assertNormalExitValue()
        } catch (pe: ProcessException) {
            throw IOException(pe)
        }

        val relativeInputFile =
            FileUtils.toSystemIndependentPath(
                FileOpUtils.makeRelative(startDir.toFile(), inputFilePath.toFile())
            )

        // send the dependency file to the processor.
        val data = dependencyFileProcessor.processFile(depFile)

        if (data != null) {
            // As of build tools 29.0.2, Aidl no longer produces an empty list of output files
            // so we need to check each file in it for content and delete the empty java files
            var isParcelable = true

            val outputFiles = data.outputFiles

            if (!outputFiles.isEmpty()) {
                for (path in outputFiles) {
                    val outputFileContent =
                        Files.readLines(File(path), Charsets.UTF_8)
                    val emptyFileLine =
                        "// This file is intentionally left blank as placeholder for parcel declaration."
                    if (outputFileContent.size <= 2
                        && outputFileContent.get(0) == emptyFileLine
                    ) {
                        FileUtils.delete(File(path))
                    } else {
                        isParcelable = false
                    }
                }
            }

            val isPackaged = nonNullPackagedList.contains(relativeInputFile)

            if (packagedOutputDir != null && (isParcelable || isPackaged)) {
                // looks like a parcelable or is listed for packaging
                // Store it in the secondary output of the DependencyData object.

                val destFile = File(packagedOutputDir, relativeInputFile)
                destFile.parentFile.mkdirs()
                Files.copy(inputFilePath.toFile(), destFile)
                data.addSecondaryOutputFile(destFile.path)
            }
        }

        FileUtils.delete(depFile)
    }
}

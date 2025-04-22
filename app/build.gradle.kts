import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.application)

}
apply(plugin = "realm-android")
fun getProperty(filename: String, propName: String): String? {
    val propsFile = rootProject.file(filename)
    return if (propsFile.exists()) {
        val props = Properties().apply {
            load(FileInputStream(propsFile))
        }
        props.getProperty(propName).also {
            if (it == null) {
                println("No such property $propName in file $filename")
            }
        }
    } else {
        println("$filename does not exist!")
        null
    }
}
android {
    namespace = "com.example.hoang_movie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hoang_movie"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "TMDB_API_KEY", "\"${getProperty("local.properties", "tmdb_api_key")}\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //
    implementation ("androidx.palette:palette:1.0.0")
    implementation ("androidx.test.espresso:espresso-idling-resource:3.6.1")
    // RX
    implementation ("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("com.jakewharton.rxbinding4:rxbinding:4.0.0")
    implementation ("com.jakewharton.rxbinding4:rxbinding-appcompat:4.0.0")

    // Network
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //other
    implementation ("net.jcip:jcip-annotations:1.0")
    implementation ("com.google.guava:guava:33.4.7-android")
    implementation ("com.squareup.moshi:moshi:1.15.1")
    // Dependency Injection
    annotationProcessor ("com.google.dagger:dagger-compiler:2.56.1")
    implementation ("com.google.dagger:dagger:2.56.1")
    compileOnly ("javax.annotation:jsr250-api:1.0")
    // Retrofit 2
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.11.0")
    //stack
    implementation("com.github.Zhuinden:simple-stack:2.9.0")
    implementation("com.github.Zhuinden:simple-stack-extensions:2.3.4")

//    implementation(files("libs/slimadapter-2.1.2.aar"))

    // Unit test
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.1.0")

    // Android UI test - Espresso core
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0") {
        exclude(group = "com.android.support", module = "support-annotations")
    }

    // Espresso contrib (RecyclerView, Drawer, etc.)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.1.0") {
        exclude(group = "com.android.support", module = "support-annotations")
        exclude(group = "com.android.support", module = "appcompat")
        exclude(group = "com.android.support", module = "support-v4")
        exclude(module = "recyclerview-v7")
    }

    // AndroidX JUnit & test rules
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test:rules:1.2.0")




}
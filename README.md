# Типа ридмишка

```
└── src
    ├── androidTest
    └── main
        ├── AndroidManifest.xml
│           java/ru/xaori/intercomcall
│               ├── data
│               │   ├── di.kt
│               │   ├── pjsip
│               │   │   ├── MyAccount.kt // Наследник Аккаунта из PJSIP
│               │   │   ├── MyCall.kt // Наследник Call из PJSIP
│               │   │   └── SipManager.kt // Все что связано с PJSIP некий репозиторий
│               │   └── service
│               │       ├── fcm
│               │       │   └── MyFirebaseMessagingService.kt // Используется для старта SipService
│               │       └── pjsip
│               │           └── SipService.kt // Отвечает за пуши и слушает изменения состояний
│               ├── di.kt
│               ├── IncomingCallActivity.kt // Активити для входящего звонка
│               ├── MainActivity.kt 
│               ├── presentation
│               │   ├── di.kt
│               │   ├── screen
│               │   │   └── call // UI для входящего звонка
│               │   │       ├── CallScreen.kt
│               │           └── component
│               │               ├── CallAnswer.kt
│               │               └── CallEffect.kt
│               │   ├── state
│               │   │   └── CallUIState.kt // UI стейт для входящего звонка
│               │   ├── theme
│               │   │   ├── Color.kt 
│               │   │   ├── Theme.kt
│               │   │   └── Type.kt
│               │   └── viewmodel
│               │       └── CallViewModel.kt // для входящего звонка
│               └── SipApp.kt
        └── res
```

1. По поводу сборки если библиотека не стартанет придется билдить для этого нужно клонировать
репозиторий https://github.com/pjsip/pjproject.

2. Далее сконфигурировать PJSIP
https://docs.pjsip.org/en/latest/get-started/posix/build_instructions.html

3. После сборки нужно собрать под Java

https://docs.pjsip.org/en/latest/pjsua2/building.html#building-java-swig-module

4. после этого в pjproject-2.15.1/pjsip-apps/src/swig/java/android будет находится папка 
pjsua2 ее скопировать в свой проект
5. И последнее в градле на уровне приложения имплиментировать либу
```kotlin
dependencies {
    implementation(project(":pjsua2"))
}

// И все наверное работает))
//import org.pjsip.pjsua2.Endpoint
```
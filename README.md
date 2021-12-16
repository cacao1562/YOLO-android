
<p align="center">
<img height="120" src='./previews/icon.png'/>
</p>

<h1 align="center">YOL.O</h1><br/>
<p align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href='https://developer.android.com'><img height="20px" src='http://img.shields.io/badge/platform-android-green.svg'/></a>
<a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
</p><br/>
<p align="center">
   즉흥으로 여행을 떠나는 사람들을 위한 혼잡도기반 여행지 정보 제공 및 사용자들의 커뮤니티 공간<br/>
</p>
</br>

## Download
<a href='https://play.google.com/store/apps/details?id=com.yolo.yolo_android'><img height="80px" src='https://play.google.com/intl/en/badges/images/generic/ko_badge_web_generic.png'/></a>
<br></br><br></br>

## Architecture
![image](./previews/final-architecture.png)<br></br>
![image](./previews/mvvm.png)<br></br><br></br>
![image](./previews/paging3-library-architecture.svg)<br></br><br></br>


## Previews
<img src="./previews/playStore01.png?raw=true" width="30%"></img>
<img src="./previews/playStore02.png?raw=true" width="30%"></img>
<img src="./previews/playStore03.png?raw=true" width="30%"></img>
<img src="./previews/playStore04.png?raw=true" width="30%"></img>
<img src="./previews/playStore05.png?raw=true" width="30%"></img>
<br></br><br></br>

## Tech stack & Open-source libraries
- Minimum SDK level 23
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- JetPack
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room - local database.
  - Paging3 - helps you load and display pages of data.
  - ViewPager2 - swipe views.
  - Navigation - helps you implement navigation.
  - DataStore - data storage solution.

- Libraries
  - [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
  - [Firebase](https://firebase.google.com/docs/cloud-messaging?hl=ko) - cloud messaging, remote config.
  - [KakaoMaps](https://apis.map.kakao.com/android/) - kakao map sdk
  - [Moshi](https://github.com/square/moshi/) - A modern JSON library for Kotlin and Java.
  - [Glide](https://github.com/bumptech/glide) - loading images.
  - [licenses](https://developers.google.com/android/guides/opensource) - include open source notices.
  - [Zxing](https://github.com/zxing/zxing) - generator barcode.

<br></br>
# License
```xml
Copyright 2021 cacao1562

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

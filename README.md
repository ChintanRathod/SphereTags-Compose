# Sphere Tags Compose 

<img src ="art/SphereTags_ChintanRathod.gif" width=432 height=432> 

## Overview  

An Android library to implement an animated tags sphere

- **API SDK 28+**
- **Written in [Jetpack Compose](https://developer.android.com/compose)**
- **Supports customization**

## Usage  
```kotlin
SphereTags(
  radius = 400.00,
  tags = tagsList,
  textColor = android.graphics.Color.BLACK,
  textSize = 12.sp,
  selectedTag = {
      Toast.makeText(context, "$it selected", Toast.LENGTH_SHORT).show()
  }
)
```

## Limitations

All drawing and computation done on UI thread hence there could be frames drops on slow devices or with big amount of objects to render.

## Issues
If you find any problems or would like to suggest a feature, please feel free to file an [issue](https://github.com/ChintanRathod/SphereTags-Compose/issues)

## License

    Copyright 2020 Chintan Rathod

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.  

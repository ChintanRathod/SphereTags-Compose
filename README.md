# Sphere Tags Compose 


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

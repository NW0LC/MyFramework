[![](https://jitpack.io/v/NW0LC/MyFramework.svg)](https://jitpack.io/#NW0LC/MyFramework)
Android-MyFramework
===========================


![MyFramework Library](http://chuantu.biz/t5/95/1495865666x2890174064.png)


This project contains a lot of convenient and development of the libraries, tools, and controls.Offline popular network framework
This libraly include all important methods for serial port profile on bluetooth communication. It has built-in bluetooth device list.



Feature
--------------

• It's very easy to use for me 

• 侧滑删除

• 类似相对布局

• 通用popWindow

• 带header的viewpager

• 画板 

• 权限申请

•轮播

•沉浸状态栏

•小工具(各种工具)

•圆头像SimpleDraweeView

•崩溃dialog

•dialog

•tableLayout

•recyclerView adapter封装类

•上下滑动选择器

•网络请求

•图片加载

•回调(Rxbus 2.0)

•RxAndroid 2.0

•json解析

•ButterKnife

•星星之间的间距可调的星星控件

• 俗名：垂直跑马灯学名：垂直翻页公告

•产品标签

•圆圈加数字指示(气泡)

•蓝牙 操作库

•HTML Parser html解析

•lottie用于解析Adobe After Effects动画

•kotlin语言类库

•Anko kotlin 插件 代码布局库


Download
--------------

Maven
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

      	<dependency>
	    <groupId>com.github.NW0LC</groupId>
	    <artifactId>MyFramework</artifactId>
	    <version>1.0.21</version>
	</dependency>

```

Gradle
```
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.NW0LC:MyFramework:1.0.21'
	}
```

注意！！
--------------
• 在自己的gradle里 要加上以下代码
  用来支持realm数据库、kotlin及其扩展库，和防止support包冲突

Plugin
```
    apply plugin: 'kotlin-android'
    apply plugin: 'realm-android'
    apply plugin: 'kotlin-android-extensions'
```
防止support包冲突
```
    configurations.all {
         resolutionStrategy.eachDependency { DependencyResolveDetails details ->
             def requested = details.requested
            if (requested.group == 'com.android.support') {
                if (!requested.name.startsWith("multidex")) {
                    details.useVersion '26.0.0-alpha1'//此处的版本号可以替换
                }
            }
        }
    }
```

Simple Usage
--------------

• Import this library to your workspace and include in to your android project 
For Eclipse ADT : Download this library and import into your workspace and include this library to your project
For Android Studio : Use Gradle to download this library from Maven




License
--------------

Copyright (c) 2014 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

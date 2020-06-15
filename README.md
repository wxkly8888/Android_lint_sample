#

关于自定义lint的开发, 不是本文的重点, 网上有很多相关的博客文章,  本文主要讨论写好的自定义lint如何使用

主要有两种方案：

## 1、google的自定义方案

[http://tools.android.com/tips...](http://tools.android.com/tips/lint-custom-rules)

这个是google的官方解决方案，需要将写好的lint.jar拷贝到开发人员机器中的**.android/lint**文件夹下，再下一次运行Android Studio时，AS会检测其内的jar文件。

缺点：开发人员的机器上所有的Android工程都会受到影响，而且每个开发人员都需要拷贝到自己的机器上。

## 2、LinkedIn方案

[https://engineering.linkedin....](https://engineering.linkedin.com/android/writing-custom-lint-checks-gradle)

LinkedIn提供了另一种思路 : 将jar放到一个aar中。将需要lint检测的项目中引入这个aar，仅对当前工程有效。

google 的官方文档也提到过aar中可以包含lint.jar。https://developer.android.com/studio/projects/android-library.html#aar-contents

如图所示：

![downloadfile](/home/wxkly/图片/downloadfile.png)

但是这里没有给出如何包含aar的具体步骤。

### 2.1、aar打包

先看下完整的项目图：

![downloadfile (1)](/home/wxkly/图片/downloadfile (1).png)

如图所示：

lint_library  是**java-library**，里面存放我们写的自定义lint类。

lint_module_aar 是一个空的**Android library**，专门用来引入lint.jar并打包成aar。

打包lint.jar进aar需要在lint_module_aar的build.gradle进行配置，网上大多数文章都是这样配置，包括LinkedIn的官方文档：

![Screenshot from 2020-06-15 13-53-06](/home/wxkly/图片/Screenshot from 2020-06-15 13-53-06.png)

我之前也是这样写，但是通过这种方式发现打包出来的aar文件中并没有包含lint.jar，这个问题搞了好久（大坑啊），

最后通过查阅google的官方文档，原来google在android gradle plugin 3.4.0之上用lintChecks不再支持将lint.jar打包进aar中。

![Screenshot from 2020-06-15 14-03-42](/home/wxkly/图片/Screenshot from 2020-06-15 14-03-42.png)

[https://developer.android.google.cn/studio/build/dependencies](https://developer.android.google.cn/studio/build/dependencies)

**就是说android gradle plugin 3.4.1之后，需要使用lintPublish, 而不能用lintChecks。**

lintPublish的使用很简单，Google文档上也给出了示例：

只需在dependencies下使用lintPublish即可。

`lintPublish project(":lint_library")`

编译下lint_module_aar模块，可以发现在build/aar 目录下生成了aar文件，解压缩后可以看到里面包含了lint.jar![Screenshot from 2020-06-15 15-56-33](/home/wxkly/图片/Screenshot from 2020-06-15 15-56-33.png)

### 2.2、使用AAR文件

有本地依赖或者上传远程仓库，这里只介绍本地依赖。将上小结生成的AAR文件拷贝在app的libs文件夹。并配置app组件的build.gradle

```go
repositories { flatDir { dirs 'libs' }}dependencies { implementation (name:'lintlibrary-release', ext:'aar')}
```

到这里，就能使用自定义的lint规则了。如果不生效，重启Android Studio看看。

### 2.3、demo地址


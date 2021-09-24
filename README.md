# LiteAPM
LiteAPM used to Android Permform manager

block-detect and asm-detect can be used to detect the block code

you can used block-detect like this:
 ```java
 implementation 'io.github.humorsmith:block-detect:1.0.0'
 ```
 install in android code:
```java
  BlockDetectInstall.install(this).detectBlock(block = {
      Log.d(TAG, "detect block =>\n$it")
  }).threshHold = 1000
```

anr-detect can be used to detect the android anr
you can use anr-detect like this:
```java
implementation 'io.github.humorsmith:anr-detect:1.0.0'
```
install in android code:
```java
AnrDetectInstall.install(this).detectAnr(block = {
    Log.d(TAG, "detect anr =>\n$it")
})
```



The asm-detect module has not been developed yet, look forward to it
```java
implementation 'io.github.humorsmith:asm-detect:1.0.0'
```

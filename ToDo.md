# ToDo





## Master



## HotFix



## Develop

## UIdesign

- [ ] 优化当所有part完成后提交到Release之前, 再进行①各个part的逻辑优化②更加精致的界面设计③动画的设计

#### 可行性检验

- [ ] 检验可行性: 当关闭窗口A时, 能否响应窗口B
- [ ] 检验可行性: 关于如何实现隐藏到任务栏和从任务栏打开指定窗口

#### 跳出界面设计

- [ ] 在WordArea界面上设计两个按钮, 点击时能响应NoteArea和WordBookArea
- [ ] WordBookArea的界面设计
- [ ] NoteArea的界面设计

#### 主界面设计

- [ ] MainArea的设计[未定稿]

## underlineWordsSolution

- [ ] 协调两种不同的划线取词的方式, 从而在保证划线取词速度的同时, 极大幅度提高准确率
- [ ] 合并两种不同划线取词的方式=>当ByClipboard获取到的内容为乱码时才对ByOCR进行分析, 在此之前ByOCR只完成截图的操作

####  UnderLineWordByOCR

- [ ] 当选中区域过于小时, 应当适当扩大
- [ ] 优化`THRESHOLD_TO_PIXEL`和`COLOR_CHIP_SIZE`两个变量在不同背景下的动态取值

####  UnderLineWordByClipboard

- [ ] 制作用Robot执行`CTRL+C`并读取剪切板的划线取词
- [ ] 注意只有当鼠标左键按下时, 再开始实时读取Glipboard从而提高后台占用率


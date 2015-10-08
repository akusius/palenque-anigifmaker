# PalenqueAniGifMaker

[User guide](http://akusius.github.io/palenque/appagm.html) |
[Latest release](https://github.com/akusius/palenque-anigifmaker/releases/latest)

This Java application is part of the [Palenque Code](http://akusius.github.io/palenque/) project.  
It can be used to create animated GIFs for demonstrating the solution process.

### Technology
Java 7 / Swing / Ant / NetBeans 8.  
No external third party library is needed for compiling and/or running.

#### Integrated third party codes
- AnimatedGifEncoder class by [Kevin Weiner, FM Software](http://www.java2s.com/Code/Java/2D-Graphics-GUI/AnimatedGifEncoder.htm)
(the quantization routines are not used, instead a global color (grayscale) table is applied to each frame);
- stripped down version of the [JSON.org Java library](http://www.json.org/java/)
(for saving and loading the settings);
- [Matrix.java](http://mrl.nyu.edu/~perlin/render/Matrix.java) by [Ken Perlin](http://mrl.nyu.edu/~perlin/);
- ChangeSupport.java by Andrei Badea;
- Base64.java backported from JDK8.

### Notes
The generated GIF files are not optimized (i.e. each frame is encoded in its entirety).  
To compress an animated GIF, e.g. the free and open source [Gifsicle](http://www.lcdf.org/gifsicle/) can be used (with the switch -O3).

The source comments are in Hungarian language.  
(Initially the entire application was in Hungarian, later the UI was translated.)

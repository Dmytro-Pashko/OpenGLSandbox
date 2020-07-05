# LibGDX Sandbox

The 3D engine prototype based on LibGDX(OpenGL) ([Link](https://https://libgdx.badlogicgames.com)) and Kotlin ([Link](https://https://kotlinlang.org)).

From the beginning, the main idea of this repository was analyzing the LibGDX functionality and performance, especially in the 3D graphic. 

After some exploration, I realized that I need a deep knowledge of 3D graphic basics, mathematical techniques(vectors, matrices, quaternions, etc.) and OpenGL API in order to build effective 3D applications. 
And this repository created to obtain some experience in 3D graphics.

The significant part of the OpenGL investigating is its own shaders and tools implementations for interact with GPU and rendering pipeline.
In each sample, I'm trying to implement everything myself to better understand the rendering process and OpenGL API.

In the beginning was the Word[John 1:1]

I've started with the 3D world editor which can contain the basic functionality to effective and optimized 3D scene creation.

The mainline of this repository is the editor branch([LINK](https://github.com/Dmytro-Pashko/GdxSandbox/tree/feature/editor)) that will contain the world editor with useful shaders, utilities, additional custom implementations of the pathfinding system, texture generation, noise generation, shaders and etc.

# The OpenGL API research:
Currently, I've implemented few test examples in onder to analyze the general functions, such as vertex transformation and primitive rasterization,
which are performed by 3D graphics hardware using the OpenGL library API.

These scenes based on direct communication with HAL(Hardware Abstraction Layer) provided by LWJGL [LINK](https://www.lwjgl.org/) for Java.

**Note:**
*To simplify code I've used a LibGDX API. Understanding of the basic the OpenGL functionality and most of the rendering parts were implemented by self.*

The following implemented scenes:
//TBD

# Resources:
All project assets downloaded from free resources(but you can contact me in any copyright issues) or created by myself.

# License:
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.

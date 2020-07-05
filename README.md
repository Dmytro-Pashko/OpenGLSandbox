# LibGDX Sandbox

The 3D engine prototype based on LibGDX(OpenGL) ([Link](https://https://libgdx.badlogicgames.com)) and Kotlin ([Link](https://https://kotlinlang.org)).

The main purpose of this repo is to analyze the LibGDX functionality and performance in the 3D graphic. 

After some exploration, I realized that I need a deep knowledge of mathematical techniques(vectors, matrices, quaternions, etc.) in order to build effective 3D applications. And so the main purpose of this repository is to obtain some experience in 3D graphics.
I decided to explore the functionality of the OpenGL using my own tools to work with rendering pipeline.

The mainline of this repository is the editor branch that will contain the simple 3D world editor and useful shaders, utilities, additional custom implementations(pathfinding system, texture generation, noise generation, shaders and etc.).

# The OpenGL research:
Currently, I've implemented few test examples in onder to analyze the general functions, such as vertex transformation and primitive rasterization,
which are performed by 3D graphics hardware using OpenGL API.

These scenes based on "clear" communication with OpenGL shaders(vertex, fragment) implementation.

**Note:**
*To simplify code I've used a LibGDX API. Understanding of the basic the OpenGL functionality and most of the rendering parts were implemented by self.*

The following implemented scenes:
//TBD


# Resources:
All project assets downloaded from free resources(but you can contact me in any copyright issues) or created by myself.

# License:
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.

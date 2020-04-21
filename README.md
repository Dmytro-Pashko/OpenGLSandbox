# LibGDX Sandbox

The 3D engine prototype based on LibGDX ([Link](https://https://libgdx.badlogicgames.com)) and Kotlin ([Link](https://https://kotlinlang.org)).

The main purpose of this repo is to exploring and analyzing the Core API of LibGDX.

In fact, I realized that I need a deep knowledge of the Math(Vectores, Matrices) OpenGL 
rendering process and the basic logic of GLSL shaders after analyzing a LibGDX classes.

# Main point
The main idea is to research basic prototype that can provide the flexible core functionality of the 3D engine:

* Maintainable engine API;
* Resource loading(models, textures, materials, sounds, scenes, configurations) from the file storage.
* Rendering API for 2D(UI widgets and debug info) and 3D scenes(3d objects, light, shadows, materials, filtering).
* Object generations(textures, meshes, models);
* UI framework to create basic customizable UI widgets(button, label, list, spinner, chooser panels etc.) and UI editor;
* Async invocations and multithreading, time events;
* Physic and AI simulation;
* UI builder and scene editor;
* Sound engine;

The mainline of this repository is the master branch that will contain the main feature implementations except debugging or test scene.
utilities, additional custom implementations(path finding, texture generation, noise generation, shaders and etc.).

# OpenGL exploration:
Currently, I've implemented few scenes to analyze OpenGL rendering process, 
this samples based on native "OpenGl" rendering process with custom vertex and fragment shaders implementation.

**Note:**
*To simplify this examples I've used a LibGDX API(Loaders, Math API, Pixmap and Texture), 
but this does not affect the understanding of the basic OpenGL functionality and most of the rendering parts were implemented by self.*

1. The 3D render to traw textured plane with rotation around Z axis.([link](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/gltest/core/src/com/dpashko/sandbox/scene/plane3d/Plane3dScene.kt))
![](https://github.com/Dmytro-Pashko/GdxSandbox/raw/master/description/plane_scene.gif)

2. The 3D render to draw textured boxes in the 3D space with random rotations with Perspective Camera view.([link](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/gltest/core/src/com/dpashko/sandbox/scene/box3d/Box3dScene.kt))
![](https://github.com/Dmytro-Pashko/GdxSandbox/raw/master/description/box_scene.gif)

3. The 3D test scene to test process of loading 3D objects with texture wrapping and custom matertials, enviroment(light) simulation and  custom camera input controller(movement - (WASD + mouse 1), horizontal rotation - mouse 2, zoom - mouse 3).([link](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/gltest/core/src/com/dpashko/sandbox/scene/model3d/Model3dScene.kt))
![](https://github.com/Dmytro-Pashko/GdxSandbox/raw/master/description/model_scene.gif)

# Resources:
All project assets downloaded from free resources(but you can contact me in any copyright issues) or created by myself.

# License:
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you can use it free of charge, without strings attached in commercial and non-commercial projects except assets(fonts, 3d models, textures, sounds, images, shaders) licensed under [CC BY-NC 3.0](https://creativecommons.org/licenses/by-nc-sa/3.0/) license for non-commercial purposes.

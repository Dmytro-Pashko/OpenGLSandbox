# LibGDX Sandbox

The 3D engine prototype based on LibGDX(OpenGL) ([Link](https://https://libgdx.badlogicgames.com)) and Java+Kotlin ([Link](https://https://kotlinlang.org)).

From the beginning, the main idea of this repository was an analysis of the LibGDX functionality and performance, especially in the 3D graphic.

After some exploration, I realized that I need a deep knowledge of 3D graphic basics, mathematical techniques(vectors, matrices, quaternions, etc.) and experience with the OpenGL API in order to build effective 3D applications. 
After all, this repository created to obtain some experience in 3D graphics.

I've started with the editor for a 3D scenes which can contain the basic functionality to effective and optimized 3D scene creation.

The mainline of this repository is the editor branch([LINK](https://github.com/Dmytro-Pashko/GdxSandbox/tree/feature/editor)) that will contain the world editor with useful GLLS shaders, utilities, additional custom implementations of the pathfinding system, texture generation, noise generation, shaders and etc.
# The OpenGL API research:
The significant part of the OpenGL analyzing is its own GLSL shaders implementation in order to interact with the OpenGL rendering process.
In each sample, I'm trying to implement everything myself to better understand the  OpenGL functionality.

Currently, I've implemented few test examples in order to analyze the general functions, such as vertex transformation and primitive rasterization, coordinate system and projection view, texturing, and light which are performed by 3D graphics hardware using the OpenGL library API.

These scenes based on direct communication with HAL(Hardware Abstraction Layer) provided by LWJGL [LINK](https://www.lwjgl.org/) for Java.
**Note:**
*To simplify code I've used a LibGDX API. Understanding of the basic the OpenGL functionality and most of the rendering parts were implemented by self.*
# Plane 3D
This example demonstrates basic techniques for working with 3D graphics:
1. The Vertex data creation;
2. The vertex and fragment shaders implementation using GLSL;
3. World transformation;
4. Perspective projection;
5. Binding the Vertex Data and texture with the Shader Program;

These are the most essential things for working with 3D graphics and understanding the rendering process.
## The Vertex Data creation:
To start drawing something we have to first give OpenGL some input vertex data. OpenGL is a 3D graphics library so all coordinates that we specify in OpenGL are in 3D (x, y, z). OpenGL doesn't simply transform all your 3D coordinates to 2D pixels on your screen; OpenGL only processes 3D coordinates when they're in a specific range between -1.0 and 1.0 on all 3 axes (x, y, z).

We manage this vertex data via so-called Vertex Buffer Objects (**VBO** [LINK](https://www.khronos.org/opengl/wiki/Vertex_Specification#Vertex_Buffer_Object)) that can store a large number of vertices in the GPU's memory.
A Vertex Array Object (**VAO** [LINK](https://www.khronos.org/opengl/wiki/Vertex_Specification#Vertex_Array_Object)) can be bound just like a vertex buffer object and any subsequent vertex attribute calls from that point on will be stored inside the VAO. 

The LibGDX provides an API to simplify vertex data creation and binding to VAO.
The **VertexBufferObjectWithVAO** ([LINK](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/VertexBufferObjectWithVAO.html)) provides methods to define the VBO with specific vertex attributes ([LINK](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/VertexAttribute.html)).
An important part of the VertexBufferObjectWithVAO functionality is auto binding an array of vertices to the VAO after initializing or updating the vertex data.

The following example creates VBO(Vertex buffer object) with VAO(Vertex Array Object) in order to define vertex data of square plane:
```
VertexBufferObjectWithVAO(true, 4, VertexAttribute.Position(), VertexAttribute.TexCoords(0)).apply {
    val vertices = floatArrayOf(
    // Position(x,y,z), Texture coordinates (u,v)
        -1f,  1f, 0f, 0f, 1f, // ╔ upper left corner
        -1f, -1f, 0f, 0f, 0f, // ╚ lower-left corner
         1f, -1f, 0f, 1f, 0f, // ╝ right bottom corner
         1f,  1f, 0f, 1f, 1f) // ╗ upper right corner
        setVertices(vertices, 0, vertices.size)
    }
```
As result the array contains 4 vertices and each vertex combines from 5 float values (x, y, z, u, v).
Our vertex buffer data is formatted as follows:
![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/vertex%20data%20format.png)

As you can see, the vertices data is a single array with specific Vertex Attributes that used to define the following vertex components:
* **VertexAttribute.Position()** - the position component(x,y,z);
* **VertexAttribute.TexCoords(0)** - the texture coordinates component(u,v).
Coordinates component is a range from 0 to 1 in the x and y axis (remember that we use 2D texture images). Texture coordinates start at (0,0) for the lower-left corner of a texture image to (1,1) for the upper right corner of a texture image:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/uv_coordinates.png)

The OpenGL can work with different types of primitives: points, lines, triangles, etc.
The most suitable primitive to build our square plane is a triangle.
Our shape has 4 vertices combined from 2 triangles, in order to avoid vertex duplication we need to define a triangle indices:
```
// Plane mesh separated by two triangles.                   0╔═╗3
// Each triangle contains 3 vertices (0 1 2) and (2 3 0).   1╚═╝2
private fun createIndices() = IndexArray(6).apply {
       setIndices(shortArrayOf(0, 1, 2, 2, 3, 0), 0, 6)
}
```
All triangle indices should be defined in counter-clockwise winding order to 
provide the correct operation of the OpenGL Face culling ([LINK](https://www.khronos.org/opengl/wiki/Face_Culling)).

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/winding-order-triangle.png)

More detailed about vertex data creation you can read at [learnopengl.com](https://learnopengl.com/Getting-started/Hello-Triangle)
## Shaders
The shader is a program that runs for each specific section of the graphics pipeline. In a basic sense, the shader is a program transforming inputs to outputs. The shader is a very isolated program and can communicate with other shaders via inputs and outputs. Shaders are written in the C-like language GLSL [Wiki](https://en.wikipedia.org/wiki/OpenGL_Shading_Language). GLSL is tailored for use with graphics and contains useful features specifically targeted at vector and matrix manipulation.
### Vertex Shader:
The Vertex Shader ([LINK](https://www.khronos.org/opengl/wiki/Vertex_Shader)) is the programmable Shader stage in the rendering pipeline that handles the processing of individual vertices.

The basic vertext shader:
```
#version 330 core
layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texCoord0;
out vec2 texcoord;
uniform mat4 combined;

void main()
{
    gl_Position = combined * vec4(a_position, 1.0f);
    texcoord = a_texCoord0;
}
```
* **a_position** - the input value of the vertex coordinates(x,y,z).
* **a_texCoord0** - the input value of the texture coordinates(u,v).
* **texcoord** - the output value of texture coordinates, will be passed to the fragment shader in order to define pixel color.
* **combined** - a global shader variable for final perspective projection matrix ([More detailed informaiton](https://hackmd.io/lB66PzAkRZmJDKkCrk4x2g?both#World-transformation-and-Projection-view)).

To set the output of the vertex shader we have to assign the position data to the predefined **gl_Position** variable which is a vec4 behind the scenes. 
gl_Position is a [Homogeneous coordinates](https://en.wikipedia.org/wiki/Homogeneous_coordinates). Homogeneous coordinates are needed for perspective projection.
### Fragment Shader:
The fragment shader is the second and final shader that should be created for rendering a textured plane. The fragment shader is all about calculating the color output of your pixels.
The fragment shader only requires one output variable and that is a vector of size 4 that defines the final color output.

The basic fragment shader:
```
#version 330 core
in vec2 texcoord;
uniform sampler2D texture_1;

void main()
{
    gl_FragColor = texture2D(texture_1, texcoord);
}
```
The texture2D function returns a texel, i.e. the (color) value of the texture for the given coordinates. The function has two parameters: the first parameter is the sampler2D uniform the texture is bound to,  and the second parameter is the 2-dimensional coordinates(u,v) for the texel lookup.

* **texcoord** - the input value of the coordinates for texture mapping obtain from vertex shader;
* **texture_1** - a global shader variable for single 2D texture;
* **gl_FragColor** - the output variable and that is a vector of size 4 that defines the final color output that we should calculate.
### Shader program:
A shader program encapsulates a vertex and fragment shader pair linked to form a shader program.
The LibGDX provides the own implementation of the [ShaderProgram](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShaderProgram.html).

The following code demonstrates the process of the Shader Program creation and linking fragment and vertex shaders created before:
```
final String vertexShader = FileProvider.simple3d_vertex_shader.readString();
final String fragmentShader = FileProvider.simple3d_fragment_shader.readString();
return new ShaderProgram(vertexShader, fragmentShader);
```

The detailed information about shaders you can find at [learnopengl.com](https://learnopengl.com/Getting-started/Shaders)
### World transformation
However, to fully understand transformations we first have to delve a bit deeper into vectors, matrices, and transformations. 
I'm not going to write about it now, so it's better to read the elaborate article at [learnopengl.com](https://learnopengl.com/Getting-started/Transformations)

### Coorination system and perspective projection
To transform the coordinates from one space to the next coordinate space we'll use several transformation matrices of which the most important are the model, view and projection matrix. Our vertex coordinates first start in local space as local coordinates and are then further processed to world coordinates, view coordinates, clip coordinates and eventually end up as screen coordinates.
The following image displays the process and shows what each transformation does:
![](https://i.imgur.com/8XqEVUw.png)
*Image from the learnopengl.com*

Detailed information about coordination system and perspective projection you can find at [learnopengl.com](https://learnopengl.com/Getting-started/Coordinate-Systems)

Briefly, to start drawing in 3D we'll first create a model matrix. The model matrix consists of translations, scaling and/or rotations we'd like to apply to transform all object's vertices to the global world space. Lest place our plane at (0,0,0). The model matrix then looks like this:
```
// Plane position and rotation
private val modelMatrix = Matrix4()
    .translate(0f, 0f, 0f)
```
By multiplying the vertex coordinates with this model matrix we're transforming the vertex coordinates to world coordinates.

Next we need to create a view matrix. We want to move slightly backwards in the scene so the object becomes visible (when in world space we're located at the origin (0,0,0)):
```
// Camera position and rotation
private val viewMatrix = Matrix4()
    .setToLookAt(Vector3(0f, 0f, 3f), Vector3.Zero, Vector3.Y)
```
The last thing we need to define is the projection matrix. We want to use perspective projection for our scene so we'll declare the projection matrix like this:
```
// Camera projection(frustum)
private val projectionMatrix = Matrix4().apply {
    val near = 0.1f
    val far = 10f
    val fov = 67f
    val aspectRation = (Gdx.graphics.width / Gdx.graphics.height).toFloat()
    setToProjection(near, far, fov, aspectRation)
    }
```
All parameters that have been passed to the **setToProjection** method are responsible for the camera frustum:
![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/frustum.png)

The frustum is used to determine what the camera can see. Everything that is inside or intersects with the camera's view-frustum has to be rendered, all the rest can be ignored.

We create a transformation matrix for each of the above-mentioned steps: model, view and projection matrix.
After all, let's define the final combined matrix, declared a uniform in the vertex shader and sent the matrix to the shaders where we transform our vertex coordinates. 
```
combinedMatrix.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix)
shader.setUniformMatrix("combined", combinedMatrix)
```
More detauled information about camera and perspective projection you can read at following articles 
* [Coordinate Systems](https://learnopengl.com/Getting-started/Coordinate-Systems)
* [Working with Camera](https://learnopengl.com/Getting-started/Camera)
### Putting it all together
The following code represents final rendering method:
```
fun draw() {
    Gdx.gl.glEnable(GL_DEPTH_TEST)
    Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    shader.begin()
    //Caclulates the combined projection and view matrix.
    combinedMatrix.set(projectionMatrix).mul(viewMatrix).mul(modelMatrix)
    // Setup the combined matrix into uniform variable "combined" in vetex shader.
    shader.setUniformMatrix("combined", combinedMatrix)
    // Bind active GL_TEXTURE_2D to texture Object handle.
    Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
    // Bind shape vertices to shader.
    vertices.bind(shader)
    // Draw draw triangles.
    Gdx.gl.glDrawElements(GL_TRIANGLES, vertices.numVertices, GL_UNSIGNED_SHORT, indices.buffer)
    // Unbind vertices.
    vertices.unbind(shader)
    shader.end()
}
```
## Result
![Gif](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/plane3d.gif)

The full code of this scene you can find at following link [Plane3dScene.kt](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/testing/core/src/com/dpashko/sandbox/scene/plane3d/Plane3dScene.kt)
# Resources & References:
All project assets from free resources(but you can contact me in any copyright issues) or created by myself.

* Best portal for learning the OpenGL [learnopengl.com](https://learnopengl.com/);

# License:
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.

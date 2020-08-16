# The OpenGL introduction
In this chapter we will analyze the rendering pipeline. It covers general functions, such as primitive rasterization, vertex data creation and transformation and the basic shading. These things are the most essential things for understanding the rendering process.

In fact, our application communicates with the GPU by sending commands and data to the OpenGL, which in turn sends commands and data to a driver that knows how to interact with the GPU on the native layer. The interface to OpenGL is called a Hardware Abstraction Layer (HAL) because it provides a common API that can be used to render a scene on any graphics hardware that supports OpenGL architecture. The LibGDX includes [LWJGL](https://www.lwjgl.org/)(Lightweight Java Game Library) that provides direct access to low-level OpenGL library and high-performance, yet also wrapped in a type-safe and user-friendly layer, appropriate for the Java ecosystem. The GPU driver translates the OpenGL function calls into code that the GPU can understand. Moreover, driver implements OpenGL functions directly to minimize the overhead of rendering commands.

The following image demonstrates this communication between application and GPU:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/communication_flow.png)

A GPU has its own memory, which is commonly called VRAM (Video Random Access Memory). The GPU may store any information in VRAM, but there are several types of data that can almost always be found in  memory when a 3D graphics application is running.

Most importantly, VRAM contains the **image buffer**. The image buffer contains the exact pixel data that is visible in the viewport. The viewport is
the area of the display containing the rendered image and may be a subregion of a window, the entire contents of a window, or the full area of the display.

Also VRAM contains a block of data called the **depth buffer**(Z-buffer).
The depth buffer stores, for every pixel in the image buffer, a value that represents how far away the pixel is or how deep the pixel lies in the image. More detailed information about depth buffer you can find at: [LINK](https://www.opengl.org/archives/resources/faq/technical/depthbuffer.htm) or [LINK](https://en.wikipedia.org/wiki/Z-buffering).

Finally, the usage of VRAM is dominated by **texture maps**. Briefly, the texture maps are images that are applied to the surface of an object to give it greater visual detail. This process calls texture mapping. More detailed information about texture mapping you can find at [LINK](https://en.wikipedia.org/wiki/Texture_mapping)

## The Vertex Data:
A typical scene is composed of many objects. The geometrical appearance of these objects is represented by a group of vertices and a particular type of basic graphic primitive, which indicates how the vertices are connected to represent a shape.

The following picture illustrates basic primitives defined by the OpenGL:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/opengl_primitives.png)

To start drawing something we have to pass vertex data into OpenGL.
OpenGL is a 3D graphics library so all coordinates that we specify in OpenGL are in 3D (x, y, and z coordinate). OpenGL provides a convenient way to pass defined vertex data to the first process of the graphics pipeline - the vertex shader. This is done by creating memory on the GPU where we store the vertex data, configure how OpenGL should interpret the memory, and specify how to send the data to the graphics card. The vertex shader then processes as many vertices as we tell it to from its memory.

We manage this memory via so-called **Vertex Buffer Objects** ([VBO](https://www.khronos.org/opengl/wiki/Vertex_Specification#Vertex_Buffer_Object)) that can store a large number of vertices in the GPU's memory. The advantage of using VBO is that we can send large batches of data all at once to the graphics card. The LibGDX provides an API to simplify the creation of  a VBO.
The **VertexBufferObjectWithVAO** ([LINK](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/VertexBufferObjectWithVAO.html)) class encapsulates  the VBO creation, sending the created VBO to the GPU after initializattion or update. In addition, the VertexBufferObjectWithVAO contains a Vertex Attribute Object for our VBO and provides an API for binding these vertex attributes to shaders.
The obvious question is, what is the Vertex Attributes Object? :)

Since VBO is a simple float array, the **Vertex Array Object** (VAO) is an object that describes how the vertex attributes are stored in a VBO. This means that the VAO is not the actual object storing the vertex data, but the descriptor of the vertex data. Therefore, we have to specify how OpenGL should interpret the vertex data before rendering.
 
We have to define the VAO in order to specify the following VBO format:
![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/vertex%20data%20format.png)

A VertexBufferObjectWithVAO class provides a convenient way to create the VBO with specific VAO.
The following code demonstates vertex data creation using  VertexBufferObjectWithVAO:
```
private fun createVertices(): VertexData {
    val vertices = floatArrayOf(
    // Position(x,y,z), Texture coordinates (u,v)
        -1f, 1f, 0f, 0f, 1f,
        -1f, -1f, 0f, 0f, 0f,
        1f, -1f, 0f, 1f, 0f,
        1f, 1f, 0f, 1f, 1f)
    return VertexBufferObjectWithVAO(true, 
                                    vertices.size, 
                                    VertexAttribute.Position(), 
                                    VertexAttribute.TexCoords(0)).apply {
        setVertices(vertices, 0, vertices.size)
    }
}
```
As you can see, VAO has two components:
* **VertexAttribute.Position()** - the position component is composed from three(x,y,z) floating-point (4 byte) values;
* **VertexAttribute.TexCoords(0)** - the texture coordinates component is composed of two(u,v) floating-point (4 byte) values.
The texture coordinates component is a range from 0 to 1 in the x and y axis (remember that we use 2D texture images). Texture coordinates start at (0,0) for the lower-left corner of a texture image to (1,1) for the upper right corner of a texture image:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/uv_coordinates.png)

## Indices
In our understanding, our shape defined by 4 vertices. But, the OpenGL interprets the vertices as a sequence of triangles, points, or lines. For us, the most suitable primitive is a triangle formed by 3 vertices. For defining triangles we will use indices. The indices control what order the vertices are received in, and indices can specify the same array element more than once:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/indices.png)

The following code snippet creates the indices data for our plane:
```
private fun createIndices(): IndexData {
    val indices = shortArrayOf(0, 1, 2, 2, 3, 0)
    return IndexArray(indices.size).apply {
    setIndices(indices, 0, indices.size)
    }
}
```
We're done with creating geometry data for our shape.

## World transformation

The vertices of a model are typically stored in local space, a coordinate system that is local to the particular model and used only by that model. But, the position and orientation of model are stored in world space, a global coordinate system that ties all of the object spaces together. To transform the coordinates from one space to the next coordinate space we'll use several transformation matrices of which the most important are the model, view and projection matrix. 

I'm sure that you have a question: What are the matrices and why we are using them?
Mathematically, a matrix is a rectangular grid of numbers, but in computer graphics, it is powerful construction that provides the ability to convert geometric data into different coordinate systems. 

Our vertex coordinates first start in local space as local coordinates and are then further transformed to world coordinates, view coordinates, clip coordinates. The following image displays the process and shows what each transformation does:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/space_transformations.png)

A thorough explanation of basic matrix math and linear algebra is beyond the scope of this article. 
You can find detailed informattion about matrices and transformation on the following links:
* [learnopengl.com](https://learnopengl.com/Getting-started/Transformations)
* [solarianprogrammer.com](https://solarianprogrammer.com/2013/05/22/opengl-101-matrices-projection-view-model)
* [opengl.org](https://www.opengl.org/archives/resources/faq/technical/transformations.htm)
* [haroldserrano.com](https://www.haroldserrano.com/blog/matrices-in-computer-graphics)

At the beginning, we have to define a model matrix that represents the position and orientation of our plane in world space:
```
 private val modelMatrix = Matrix4()
```
Also, we should define a function that rotates our plane around an arbitrary axis in world space:
```
fun rotate(axis: Vector3, degree: Float) {
 modelMatrix.rotate(axis, degree)
}
```
Actually, this rotation is just the multiplication of our model matrix by a rotation matrix. A rotation matrix is defined for each axis in 3D space where the angle is represented as the theta symbol θ:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/rotation_matrices.png)

Using the rotation matrices we can rotate our plane around one of the three axies. But, to rotate around an arbitrary 3D axis we should combine all 3 them:

![](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/combined_rotation_matrix.png)

 More detailed information about the rotation matrix you can find at [LINK](https://en.wikipedia.org/wiki/Rotation_matrix).

Before an object can be rendered, its vertices must be transformed into camera (also called eye space), the space in which the x and y axes are aligned to the display and the z axis is parallel to the viewing direction.

For this purposes we have to define a camera matrix:
```
// Camera position and rotation matrix
 private val viewMatrix = Matrix4()
 .setToLookAt(Vector3(0f, 0f, 3f), Vector3.Zero, Vector3.Y)

// Camera projection(frustum)
private val projectionMatrix = Matrix4().apply {
    val near = 1f
    val far = 5f
    val fov = 67f
    val aspectRation = (Gdx.graphics.width / Gdx.graphics.height).toFloat()
        setToProjection(near, far, fov, aspectRation)
    }

// Combined camera matrix.
private val cameraMatrix = Matrix4().set(projectionMatrix).mul(viewMatrix)
```

The following image illustrates the location of our plane and camera in the world space defined by model and camera matrices:

![](https://github.com/Dmytro-Pashko/OpenGLSandbox/raw/chapter_1/description/scene.png)

Also, the camera matrix includes a projection transformation that has the effect of applying perspective so that geometry becomes smaller as the distance from the camera increases.
The detailed description of camera parameters will be described in next chapter. :)

The advantage from using matrices for transformations is that we can combine multiple transformations in a single matrix. This will reduce the number of matrices we need to pass to the shader. Also, matrix multiplication is not commutative, which means their order is important:
```
val combinedMatrix = Matrix4().set(cameraMatrix).mul(modelMatrix)
```
Finally, when the combined matrix is defined we have to pass it to vertex shader using uniforms:
```
shader.setUniformMatrix("combined", combinedMatrix)
```
In vertex we will obtrain this matrix via 'combined' uniform:
```
uniform mat4 combined;
```

## Shaders
The shader is a program that runs for each specific section of the graphics pipeline. In a basic sense, the shader is a program transforming inputs to outputs. The shader is a very isolated program and can communicate with other shaders via inputs and outputs. Shaders are written in the C-like language GLSL [Wiki](https://en.wikipedia.org/wiki/OpenGL_Shading_Language). GLSL is tailored for use with graphics and contains useful features specifically targeted at vector and matrix manipulation.
### Vertex Shader:
The Vertex Shader ([LINK](https://www.khronos.org/opengl/wiki/Vertex_Shader)) is the programmable Shader stage in the rendering pipeline that handles the processing of individual vertices.

I've implemented the simplest vertex shader we can find: the only thing it should to do is to calculate the position of the vertex considering the combined(View * Projection * Model) matrix and send the input vertices to the fragment shader without transformation. So, it’s a useless shader and doesn't provide any transformations. But it’s perfect to see the principle of its working.

The the following code snipper of vertex shader:
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
The fragment shader only requires one output variable **gl_FragColor** and that is a vector of size 4 that defines the final color output.

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
* **gl_FragColor** - the output variable of final color value that we should calculate.
### Shader program:
A shader program encapsulates a vertex and fragment shader pair which linked to form a shader program. The LibGDX provides the own implementation of the [ShaderProgram](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/glutils/ShaderProgram.html).

The following code demonstrates the process of the Shader Program creation and linking fragment and vertex shaders created before:
```
final String vertexShader = FileProvider.simple3d_vertex_shader.readString();
final String fragmentShader = FileProvider.simple3d_fragment_shader.readString();
return new ShaderProgram(vertexShader, fragmentShader);
```

Finally, in order to draw our shape, we need to pass into created shader the following things: final transformation matrix(combined), texture, and data vertices.
Let's define the draw method in Plane class in order to encapsulate its own drawing logic for each Plane object:

```
fun draw(shader: ShaderProgram, cameraMatrix: Matrix4) {
    shader.setUniformMatrix("combined", tmp.set(cameraMatrix).mul(modelMatrix))
    texture.bind(GL_TEXTURE_2D)
    vertices.bind(shader)
    Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
    vertices.unbind(shader)
}
```

The detailed information about shaders you can find at [learnopengl.com](https://learnopengl.com/Getting-started/Shaders)

## Putting it all together
Finally, The following code represents scene loop method:
```
    override fun draw() {
        rotatePlane()
        Gdx.gl.glEnable(GL_DEPTH_TEST)
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.begin()
        plane.draw(shader, cameraMatrix)
        shader.end()
    }
}
```
After starting our scene, we see the following:

![Gif](https://raw.githubusercontent.com/Dmytro-Pashko/OpenGLSandbox/chapter_1/description/final_output.gif)

The full code of this scene you can find at following link [Plane3dScene.kt](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/testing/core/src/com/dpashko/sandbox/scene/plane3d/Plane3dScene.kt)

# License:
All code samples, gifs, and images are licensed under the terms of the CC BY-NC 4.0 license as published by Creative Commons, either version 4 of the License You can find a human-readable format of the license [here](https://creativecommons.org/licenses/by-nc/4.0/) and the full license [here](https://creativecommons.org/licenses/by-nc/4.0/legalcode).
When attributing any of the licensed works, please include a copyright notice including a hyperlink to the copyrighted work and a link to the specific license.

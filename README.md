In the preceding code snippet, w

# Cubes 3D
I'm sure that you're astonished by the number of various things you need to work in order to implement a simple scene.
Actually, the plane from [Link]previous scene that rotates around the axis looks pretty simple and primitive, but it's necessary to understand the basic stages of working with the OpenGL.
In this part, we will delve into the matrices and analyze their impact in details.
### Camera
In fact, the OpenGL itself is not familiar with the concept of the real world camera, but we can try to simulate it. When we're talking about camera/view space we're talking about all the vertex coordinates as seen from the camera's perspective as the origin of the scene: the view matrix transforms all the world coordinates into view coordinates that are relative to the camera's position and direction. To define a camera we need its position in world space, the direction it's looking at and vector pointing upwards from the camera. 

Lets define camera view matrix:
```
private val viewMatrix = Matrix4().apply {
    val position = Vector3(0f, -8f, 1f)
    val target = Vector3(0f, 0f, 0f)
    val direction = Vector3(position).sub(target).nor()
    val right = Vector3.X
    val up = Vector3(direction).crs(right)
    setToLookAt(position, target, up)
}
```
* **position** - the camera position in world space;
* **target** - the camera target point;
* **direction** -  the normalized vector of the camera direction;
* **right** - a vector that represents the positive x-axis of the camera space;
* **up** - a vector pointing upwards from the camera position.

In **setToLookAt()** method we have to specify 3 parameters: a camera position, a target position and a vector that represents the up vector in world space.
Actually, this method calculates all values of camera view matrix.

This image shows the placement of objects in this scene:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/cubes3d_scene.png)

The last thing we need to define for camera is the projection matrix. The projection matrix defines a parameters of a camera frustum. And now, you have a question - "What is the Frustum?".
So, Frustum is a truncated rectangular pyramid used to define the viewable region and its projection onto the screen. In other words, frustum defines the area which will be visible in camera, let's see on the following picture:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/frustum_update.png)

Its first and second parameter set the **near** and **far** plane of the frustum.
Let's define the near plane distance to 0.1 and the far plane distance to 32.0 units, because only the vertices between the near and far plane and inside the camera frustum will be rendered.
The third parameter defines the **fov** value([LINK](https://en.wikipedia.org/wiki/Angle_of_view)), that stands for field of view(Angle of view) and sets how large the viewspace is. 
The last parameter sets the aspect ratio which is calculated by dividing the viewport's width by its height.
```
private val projectionMatrix = Matrix4().apply {
    val near = 0.1f
    val far = 32f
    val fov = 67f
    val aspectRatio = Gdx.graphics.width / Gdx.graphics.height
    setToProjection(near, far, fov, aspectRatio.toFloat())
}
```
There are all required parameters for perspective camera:

* **near** - a near plane distance of camera frustum;
* **far** -a far plane distance of camera frustum;
* **fov** - the field of view of the height, in degrees;
* **aspectRatio** - the aspect ratio of the viewport.

The **setToProjection** methods calculates all values of perspective camera matrix (we should calculate them by ourselves or use the setToProjection method in the matrix wrapper for this purpose).

More detailed information about the perspective camera you can find at [LINK](https://learnopengl.com/Getting-started/Camera).

### Model (Vertex Data)

In fact, it is obvious to use one same model (vertex data) for many objects in a scene, this aproach reduces the amount of memory used. Moreover, each object can have a unique texture, position, size and use the same vetexes data. Almost all games use this technique.

In this part we will be working with the simple 3d model - "cube". We should  create "shared" vertex data that will be used for each object in the scene.

But before creating vertex data, we need to create a data class (POJO) for our cubes that will have its own private data: position, size and texture:
```
data class Cube(val position: Vector3, val size: Float = 1f, val texture: Texture) {
    private val vertices: VertexData
    private val indices: IndexData
    private val modelMatrix: Matrix4

    fun draw(shader: ShaderProgram, cameraMatrix: Matrix4) {
        //TODO
    }
        
    fun dispose() {
        //TODO
    }
       
}
```

As I mentioned above, all our cubes should use the same "shared" model, let's create vertex data and reuse them for each cube.

Since we were working with a two-dimensional plane, even in three-dimensional space, and we should extend the 2D plane to a 3D cube. For the cube model, we need to define 24 vertices (6 faces * 4 vertices).

The following image shows the qube in three-dimensional space with edge length equal to 1.0 and origin in middle of the cube:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/cube.png)

The following code snippet represents vertices creation:

```
private fun createVertexData(): VertexData {
    val vertices = floatArrayOf(
    // Bottom face                                 ___________
    -.5f, -.5f, -.5f, 0f, 0f,   //0                |16     17|
    .5f, -.5f, -.5f, 1f, 0f,    //1                |  BACK   |
    .5f, .5f, -.5f, 1f, 1f,     //2                |         |
    -.5f, .5f, -.5f, 0f, 1f,    //3                |19_____18|
    // Top face                         _________________________________
    -.5f, -.5f, .5f, 0f, 0f,    //4     |9       8||7       6||12     13|
    .5f, -.5f, .5f, 1f, 0f,     //5     |   LEFT  ||   TOP   ||  RIGHT  |
    .5f, .5f, .5f, 1f, 1f,      //6     |         ||         ||         |
    -.5f, .5f, .5f, 0f, 1f,     //7     |10_____11||4_______5||15_____14|
    // Left face                                   ___________
    -.5f, .5f, .5f, 1f, 0f,     //8                |23     22|
    -.5f, .5f, -.5f, 1f, 1f,    //9                |  FRONT  |
    -.5f, -.5f, -.5f, 0f, 1f,   //10               |         |
    -.5f, -.5f, .5f, 0f, 0f,    //11               |20_____21|
    // Right face                                  ___________
    .5f, .5f, .5f, 1f, 0f,      //12               |0       1|
    .5f, .5f, -.5f, 1f, 1f,     //13               |  BOTTOM |
    .5f, -.5f, -.5f, 0f, 1f,    //14               |         |
    .5f, -.5f, .5f, 0f, 0f,     //15               |3_______2|
    // Back face
    -.5f, -.5f, -.5f, 0f, 1f,   //16
    .5f, -.5f, -.5f, 1f, 1f,    //17
    .5f, -.5f, .5f, 1f, 0f,     //18
    -.5f, -.5f, .5f, 0f, 0f,    //19
    // Front Face
    -.5f, .5f, -.5f, 0f, 1f,    //20
    .5f, .5f, -.5f, 1f, 1f,     //21
    .5f, .5f, .5f, 1f, 0f,      //22
    -.5f, .5f, .5f, 0f, 0f      //23
    )
    return VertexBufferObjectWithVAO(true, vertices.size, 
    VertexAttribute.Position(),VertexAttribute.TexCoords(0)).apply {
        setVertices(vertices, 0, vertices.size)
    }
}
```
In our understanding, each face of the cube is described by 4 vertices. But, the OpenGL interprets the vertices as a sequence of triangles, points, or lines. For us, the most suitable privative is a triangle formed by 3 vertices. For defining triangles we will use indices. The indices control what order the created vertices are received in, and indices can specify the same array element more than once.

Try to define the qube indices (each value is position of vertix in vertices array created above):

```
private fun createIndices(): IndexData {
    val indices = shortArrayOf(
    0, 1, 2, 2, 3, 0,       // Bottom face
    4, 5, 6, 6, 7, 4,       // Top face
    8, 9, 10, 10, 11, 8,    // Left face
    12, 13, 14, 14, 15, 12, // Right face
    16, 17, 18, 18, 19, 16, // Back face
    20, 21, 22, 22, 23, 20  // Front face
    )
    return IndexArray(indices.size).apply {
        setIndices(indices, 0, indices.size)
    }
}
```
The following image shows the triangulated faces of the cube model:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/cube_indices.png)

Each cube will look the same but will only differ in where it's located in the world with each a different rotation. The graphical layout of the cube is already defined, and we don't have to change our buffers or attribute arrays when rendering more objects. The only thing we have to change for each object is its model matrix where we transform the cubes into the world.

Let's define the model matrix that based on position, and scale factor for each axis:

```
private val modelMatrix = Matrix4()
    .translate(position)
    .scale(size, size, size)

```
We used **translate(float x, float y, float z)** ([Link](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Matrix4.html#translate-float-float-float-)) and **scale(float scaleX, float scaleY, float scaleZ)** ([Link](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Matrix4.html#scale-float-float-float-)) from the LibGDX in order to reduce the amount of work.

Information about Matrix-Vector multiplication(scaling, translation and rotation) you can find at [LINK](https://learnopengl.com/Getting-started/Transformations).

Additionally, we should add method to rorate our boxes around specified axis and angle. For this purposes we will use **rotate(Vector3 axis, float degrees)**([Link](https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Matrix4.html#rotate-com.badlogic.gdx.math.Vector3-float-)) method from the LibGDX in order to multiplies the model matrix with a (**counter-clockwise**) rotation matrix:
```
fun rotate(axis: Vector3, degree: Float) {
    modelMatrix.rotate(axis, degree)
}
```
* **axis** - The vector axis to rotate around.
* **degrees** - The angle in degrees.


Finally, in order to render each cube we should implement the **draw** method in order to encapsulate rendering logic for each cube. This method should include the following steps:
* **Calculate the combined matrix and bind it to shader program:**
```
shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(modelMatrix))
```

* **Bind vertices and texture with the shader program:**
```
Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle) 
vertices.bind(shader)
```
* **Render primitives from array data:**
```
Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
```
* **Unbind the vertices and texture**:
```
vertices.unbind(shader)
```
The following code snippet represent the final render method:
```
fun draw(shader: ShaderProgram, combinedCameraMatrix: Matrix4) {
    shader.setUniformMatrix("combined", tmp.set(combinedCameraMatrix).mul(modelMatrix))
    Gdx.gl.glBindTexture(GL_TEXTURE_2D, texture.textureObjectHandle)
    vertices.bind(shader)
    Gdx.gl.glDrawElements(GL_TRIANGLES, indices.numIndices, GL_UNSIGNED_SHORT, indices.buffer)
    vertices.unbind(shader)
    }
```
* **tmp** - variable used to store the result of matrices multiplication.

Great job, we finished with cube model.
### Putting it all together
Eventually, we defined the Cube model and now, we can create a many cubes with diffent position, size, texture and rotation.
We'll define 7 cubes with unique position, texture, and size:
```
boxes = mapOf(
    Box(position = Vector3(2f, 1f, -2f), texture = Texture(FileProvider.box1)) to getRandomDirection(),
    Box(position = Vector3(-2f, 1f, -2f), texture = Texture(FileProvider.box2)) to getRandomDirection(),
    Box(position = Vector3(0f, -2f, -2f), texture = Texture(FileProvider.box3)) to getRandomDirection(),
    Box(position = Vector3.Zero, texture = Texture(FileProvider.box4), size = 2f) to Vector3.Zero,
    Box(position = Vector3(-2f, -1f, 2f), texture = Texture(FileProvider.box5)) to getRandomDirection(),
    Box(position = Vector3(2f, -1f, 2f), texture = Texture(FileProvider.box6)) to getRandomDirection(),
    Box(position = Vector3(0f, 2f, 2f), texture = Texture(FileProvider.box7)) to getRandomDirection()
)
```

And all that is needed to create such a "complex" scene :)

### Shaders
Our shaders should draw a textured triangle, nothing special, and for this purpose, we'll use shaders from the previous chapter ([Link](https://github.com/Dmytro-Pashko/OpenGLSandbox/blob/chapter_1/README.md)).

### Render loop
Within the render loop, we want to update camera rotation around Z-axis by 0.25 degree per frame:
```
viewMatrix.rotate(Vector3.Z, 0.25f)
combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
```
And call the render method for each created box object. We also added a unique rotation axis to each box.
So, we'll create a small loop within the render loop that renders our cubes with a different model matrix and texture each time:
```
for ((box, rotationAxis) in boxes) {
    box.rotate(rotationAxis, 0.25f)
    box.draw(shader, combinedCameraMatrix)
}
```
The following code snippet represents rendering method:
```
override fun draw() {
    Gdx.gl.glEnable(GL_DEPTH_TEST)
    Gdx.gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    // Rotate camera around Z axis by 0.25 degree.
    viewMatrix.rotate(Vector3.Z, 0.25f)
    combinedCameraMatrix.set(projectionMatrix).mul(viewMatrix)
    shader.begin()
    for ((box, rotationAxis) in boxes) {
        box.rotate(rotationAxis, 0.25f)
        box.draw(shader, combinedCameraMatrix)
    }
    shader.end()
}
```
## Result
If you run this scene you should get something like this:

![](https://raw.githubusercontent.com/Dmytro-Pashko/GdxSandbox/feature/testing/description/cube3d.gif)

The full code of this scene you can find at following link [Box3dScene.kt](https://github.com/Dmytro-Pashko/GdxSandbox/blob/feature/testing/core/src/com/dpashko/sandbox/scene/cube3d/Cubes3dScene.kt)
# Resources & References:
All project assets from free resources(but you can contact me in any copyright issues) or created by myself.
# License:
This project is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html), meaning you can use it free of charge, without strings attached in commercial and non-commercial projects.

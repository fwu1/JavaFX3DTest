// code from book "JavaFX 8- Introduction by Example (2014,-409).pdf"

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Origin3D extends Application {
	private PerspectiveCamera camera;
	private final double cameraModifier = 50.0;
	private final double cameraQuantity = 10.0;
	private final double sceneWidth = 600;
	private final double sceneHeight = 600;
	private double mouseXold = 0;
	private double mouseYold = 0;
	private final double cameraYlimit = 15;
	private final double rotateModifier = 25;

	@Override
	public void start(Stage primaryStage) {
		// Step 1a: Build your Scene and Camera
		Group sceneRoot = new Group();
		Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight);
		scene.setFill(Color.BLACK);
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-1000);
		scene.setCamera(camera);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);
		
		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);
		
		
		double originAxisLength=100;
		//Step 1b: Add a primitive
		final Cylinder originAxisY = new Cylinder(5, originAxisLength);
		
		originAxisY.setMaterial(blueMaterial);
		//End Step 1b
		//Step 1c: Translate and Rotate primitive into position
		originAxisY.setTranslateY(0.5*originAxisLength);
		originAxisY.setRotationAxis(Rotate.X_AXIS);
//		cylinder.setRotate(45);
//		originAxisX.setTranslateZ(-200);
		//End Step 1c		
		

		final Cylinder originAxisX = new Cylinder(5, originAxisLength);
		originAxisX.setMaterial(greenMaterial);
		originAxisX.setTranslateX(0.5*originAxisLength);
		originAxisX.setRotationAxis(Rotate.Z_AXIS);
		originAxisX.setRotate(90);

		final Cylinder originAxisZ = new Cylinder(5, originAxisLength);
		originAxisZ.setMaterial(redMaterial);
		originAxisZ.setTranslateZ(0.5*originAxisLength);
		originAxisZ.setRotationAxis(Rotate.X_AXIS);
		originAxisZ.setRotate(90);
		
		
//		sceneRoot.getChildren().addAll(cylinder,cube,sphere);
		//End Step 1d

		//Step 1e: All Together Now: Grouped Primitives
		Group primitiveGroup = new Group(originAxisX,originAxisY,originAxisZ);
		primitiveGroup.setRotationAxis(Rotate.Z_AXIS);
//		primitiveGroup.setRotate(180); //Rotate the Group as a whole
		sceneRoot.getChildren().addAll(primitiveGroup);
		//End Step 1e
		
		primaryStage.setTitle("SimpleScene3D");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Step 2a: Primitive Picking for Primitives
		scene.setOnMouseClicked(event -> {
			Node picked = event.getPickResult().getIntersectedNode();
			if (null != picked) {
				double scalar = 2;
				if (picked.getScaleX() > 1)
					scalar = 1;
				picked.setScaleX(scalar);
				picked.setScaleY(scalar);
				picked.setScaleZ(scalar);
			}
		});
		// End Step 2a
		
		// Step 2b: Add a Movement Keyboard Handler
		scene.setOnKeyPressed(event -> {
			double change = cameraQuantity;
			// Add shift modifier to simulate "Running Speed"
			if (event.isShiftDown()) {
				change = cameraModifier;
			}
			// What key did the user press?
			KeyCode keycode = event.getCode();
			// Step 2c: Add Zoom controls
			if (keycode == KeyCode.W) {
				camera.setTranslateZ(camera.getTranslateZ() + change);
			}
			if (keycode == KeyCode.S) {
				camera.setTranslateZ(camera.getTranslateZ() - change);
			}
			// Step 2d: Add Strafe controls
			if (keycode == KeyCode.A) {
				camera.setTranslateX(camera.getTranslateX() - change);
			}
			if (keycode == KeyCode.D) {
				camera.setTranslateX(camera.getTranslateX() + change);
			}
		});
		// End Step 2b-d		
		
		// Step 3: Add a Camera Control Mouse Event handler
		Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate, yRotate);
		
		scene.addEventHandler(MouseEvent.ANY, event -> {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				// acquire the new Mouse coordinates from the recent event
				double mouseXnew = event.getSceneX();
				double mouseYnew = event.getSceneY();
				if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					// calculate the rotational change of the camera pitch
					double pitchRotate = xRotate.getAngle() + (mouseYnew - mouseYold) / rotateModifier;
					// set min/max camera pitch to prevent camera flipping
					pitchRotate = pitchRotate > cameraYlimit ? cameraYlimit : pitchRotate;
					pitchRotate = pitchRotate < -cameraYlimit ? -cameraYlimit : pitchRotate;
					// replace the old camera pitch rotation with the new one.
					
//					pitchRotate=1; // DEBUG
					xRotate.setAngle(pitchRotate);
					// calculate the rotational change of the camera yaw
					double yawRotate = yRotate.getAngle() - (mouseXnew - mouseXold) / rotateModifier;
					
//					yawRotate=1; // DEBUG
					yRotate.setAngle(yawRotate);
				}
				mouseXold = mouseXnew;
				mouseYold = mouseYnew;
			}
		});
		// End Step 3	
	}

	public static void main(String[] args) {
		launch(args);
	}
}
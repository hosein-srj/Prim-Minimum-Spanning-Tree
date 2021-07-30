package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

	ArrayList<Circle> circles = new ArrayList<Circle>();
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Text> texts = new ArrayList<Text>();
	Map<Circle, Node> circleToNode = new HashMap<Circle, Node>();
	Map<Node, Circle> nodeToCircle = new HashMap<Node, Circle>();
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Line> linesWays = new ArrayList<Line>();

	int V = 0;

	Line l;

	@Override
	public void start(Stage primaryStage) {

		Graph graph = new Graph();
		Group root = new Group();
		Scene scene = new Scene(root, 1000, 600);

		ImageView imageViewBG = new ImageView();
		Image imageBackground = new Image("file:Background2.jpg");
		imageViewBG.setImage(imageBackground);
		imageViewBG.setPreserveRatio(true);
		imageViewBG.setFitHeight(600);
		root.getChildren().add(imageViewBG);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		Button btn1 = new Button();
		btn1.setText("Maximum Matching");
		btn1.setTranslateX(350);
		btn1.setTranslateY(20);
		root.getChildren().add(btn1);

		btn1.setOnAction((ActionEvent event) -> {
			int[][] graphInt = new int[nodes.size()][nodes.size()];
			V = nodes.size();
			for (Node node1 : nodes) {
				for (Node node2 : nodes) {

					int v1 = node1.toString().charAt(0) - 'A';
					int v2 = node2.toString().charAt(0) - 'A';

					try {
						if (node1.toString().equals(node2.toString()))
							graphInt[v1][v2] = 0;
						else
							graphInt[v1][v2] = node1.adjacentNodes.get(node2);
					} catch (Exception e) {
						graphInt[v1][v2] = 0;
					}
				}
			}
			for (int i = 0; i < graphInt.length; i++) {
				for (int j = 0; j < graphInt.length; j++) {
					System.out.print(graphInt[i][j] + " ");
				}
				System.out.println();
			}
			primMST(graphInt, root);
		});

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			double x, y;

			@Override
			public void handle(MouseEvent event1) {
				String string = "A";
				string = changeString(string, circles.size());

				boolean bool = true;
				for (Circle circle : circles) {
					if (Math.abs(circle.getTranslateX() - event1.getSceneX()) <= 20
							&& Math.abs(circle.getTranslateY() - event1.getSceneY()) <= 20) {
						bool = false;
						break;
					}
				}
				x = event1.getSceneX();
				y = event1.getSceneY();
				if (bool == true) {
					Circle circle = new Circle(15);
					circle.setTranslateX(event1.getSceneX());
					circle.setTranslateY(event1.getSceneY());
					circle.setFill(Color.WHITE);
					circles.add(circle);
					root.getChildren().add(circle);
					Node node = new Node(string);
					graph.addNode(node);
					Text text = new Text(x - 5, y + 4, string);
					texts.add(text);
					root.getChildren().add(text);
					circleToNode.put(circle, node);
					nodeToCircle.put(node, circle);
					nodes.add(node);

				}

				scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						Circle sourceCircle = new Circle();
						sourceCircle = getCircle(circles, x, y);
						for (Circle circle : circles) {
							if (Math.abs(event.getSceneX() - circle.getTranslateX()) <= 12
									&& Math.abs(event.getSceneY() - circle.getTranslateY()) <= 12
									&& !equalCircle(circle, sourceCircle)) {
								Line line = new Line(sourceCircle.getTranslateX(), sourceCircle.getTranslateY(),
										circle.getTranslateX(), circle.getTranslateY());
								line.setStrokeWidth(5);
								line.setStroke(Color.WHITE);
								root.getChildren().add(line);

								for (Text text : texts) {
									root.getChildren().remove(text);
									root.getChildren().add(text);
								}
								lines.add(line);

								TextField value = new TextField();
								value.setText("");
								GridPane grid = new GridPane();
								grid.setPadding(new Insets(30, 100, 20, 500));
								Text text = new Text("Enter Weight : ");
								text.setFill(Color.WHITE);
								grid.add(text, 0, 0);
								grid.add(value, 1, 0);
								root.getChildren().add(grid);

								value.setOnKeyPressed(new EventHandler<KeyEvent>() {
									@Override
									public void handle(KeyEvent ke) {
										if (ke.getCode().equals(KeyCode.ENTER)) {
											Circle sourceCircle = getCircle(circles, x, y);

											int weigh = Integer.parseInt(value.getText());
											root.getChildren().remove(grid);
											Node sourceNode = circleToNode.get(sourceCircle);
											Node targetNode = circleToNode.get(circle);
											sourceNode.addDestination(targetNode, weigh);
											targetNode.addDestination(sourceNode, weigh);

											double deltaY = sourceCircle.getTranslateY() - circle.getTranslateY();
											double deltaX = sourceCircle.getTranslateX() - circle.getTranslateX();
											double avgY = (sourceCircle.getTranslateY() + circle.getTranslateY()) / 2;
											double avgX = (sourceCircle.getTranslateX() + circle.getTranslateX()) / 2;
											double m = deltaY / deltaX;
											double finalY = (-13 / Math.sqrt(1 + m * m)) + avgY;
											double finalX = ((13 * m) / Math.sqrt(1 + m * m)) + avgX;

											Text text = new Text(finalX - 5, finalY + 5, Integer.toString(weigh));
											text.setStyle("-fx-font: 20 arial;");
											text.setFill(Color.YELLOW);

											text.setRotate(Math.atan(m) * 180 / Math.PI);
											root.getChildren().add(text);

										}
									}
								});

							}
						}
					}
				});
			}
		});

	}

	void DrawMST(int parent[], int n, int graph[][], Group root) {
		root.getChildren().removeAll(circles);
		root.getChildren().removeAll(texts);

		for (int i = 1; i < V; i++) {
			l = new Line(nodeToCircle.get(nodes.get(parent[i])).getTranslateX(),
					nodeToCircle.get(nodes.get(parent[i])).getTranslateY(),
					nodeToCircle.get(nodes.get(i)).getTranslateX(), nodeToCircle.get(nodes.get(i)).getTranslateY());
			l.setStroke(Color.RED);
			l.setStrokeWidth(3);
			root.getChildren().add(l);
		}

		root.getChildren().addAll(circles);
		root.getChildren().addAll(texts);
	}


	void primMST(int graph[][], Group root) {
		int parent[] = new int[V];
		int key[] = new int[V];
		Boolean mstSetLable[] = new Boolean[V];

		for (int i = 0; i < V; i++) {
			key[i] = Integer.MAX_VALUE;
			mstSetLable[i] = false;
		}

		key[0] = 0;
		parent[0] = -1; // Start with Node 0

		for (int count = 0; count < V - 1; count++) {

			int u = minKey(key, mstSetLable);

			mstSetLable[u] = true;


			for (int v = 0; v < V; v++)

				if (graph[u][v] != 0 && mstSetLable[v] == false && graph[u][v] < key[v]) {
					parent[v] = u;
					key[v] = graph[u][v];
				}
		}

		DrawMST(parent, V, graph, root);
	}

	int minKey(int key[], Boolean mstSetLable[]) {
		int min = Integer.MAX_VALUE;
		int minIndex = -1;

		for (int v = 0; v < V; v++)
			if (mstSetLable[v] == false && key[v] < min) {
				min = key[v];
				minIndex = v;
			}
		return minIndex;
	}

	private Circle getCircle(ArrayList<Circle> circles, double x, double y) {
		for (Circle circle : circles) {
			if (Math.abs(x - circle.getTranslateX()) <= 12 && Math.abs(y - circle.getTranslateY()) <= 12) {
				return circle;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		launch(args);
	}

	protected String changeString(String string, int size) {
		char help;
		for (int i = 0; i < size; i++) {
			help = string.charAt(0);
			help++;
			string = "";
			string += help;
		}
		return string;
	}

	public static boolean equalCircle(Circle circle1, Circle circle2) {
		if (circle1.getTranslateX() == circle2.getTranslateX() && circle1.getTranslateY() == circle2.getTranslateY())
			return true;
		else
			return false;
	}

}

package io.cloudneutral.shortestpath.gui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import io.cloudneutral.shortestpath.graph.HeuristicCost;
import io.cloudneutral.shortestpath.graph.Node;
import io.cloudneutral.shortestpath.graph.WeightedGraph;
import io.cloudneutral.shortestpath.graph.algorithm.FringeListener;
import io.cloudneutral.shortestpath.graph.algorithm.SearchAlgorithm;
import io.cloudneutral.shortestpath.graph.grid.Cell;
import io.cloudneutral.shortestpath.graph.grid.Coordinate;
import io.cloudneutral.shortestpath.graph.grid.GridSurface;
import io.cloudneutral.shortestpath.graph.grid.Status;

public class Controller {
    private final CanvasPanel canvasPanel;

    private final JLabel statusLabel;

    private GridSurface gridSurface;

    private boolean dragging;

    private final LinkedBlockingDeque<Cell> drawingQueue = new LinkedBlockingDeque<>(100);

    private SwingWorker<?, ?> currentWorker;

    public Controller(JFrame frame, CanvasPanel canvasPanel, JLabel statusLabel) {
        this.canvasPanel = canvasPanel;
        this.statusLabel = statusLabel;

        new SwingWorker<Void, Cell>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!isCancelled()) {
                    publish(drawingQueue.take());
                }
                return null;
            }

            @Override
            protected void process(List<Cell> chunks) {
                for (Cell cell : chunks) {
                    gridSurface.getGraph().node(cell).setStatus(Status.obstacle);
                }
                canvasPanel.repaint(7);
            }
        }.execute();

        canvasPanel.addMouseMotionListener(new MouseMotionAdapter() {
            Coordinate lastPos;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (gridSurface != null) {
                    Coordinate pos = gridSurface.toCellPosition(e.getX(), e.getY());
                    if (dragging && !pos.equals(lastPos)) {
                        lastPos = pos;
                        drawingQueue.push(Cell.of(pos.getX(), pos.getY()));
                    }
                }
            }
        });

        canvasPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragging = true;
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (gridSurface == null) {
                    return;
                }
                Coordinate pos = gridSurface.toCellPosition(e.getX(), e.getY());

                Cell cell = gridSurface.getGraph().node(Cell.of(pos.getX(), pos.getY()));

                if (cell.getStatus() == Status.empty) {
                    Optional<Cell> start = findStartCell(gridSurface.getGraph());
                    if (!start.isPresent()) {
                        cell.setStatus(Status.start);
                    } else {
                        Optional<Cell> goal = findGoalCell(gridSurface.getGraph());
                        if (!goal.isPresent()) {
                            cell.setStatus(Status.goal);
                        } else {
                            cell.setStatus(Status.obstacle);
                        }
                    }
                } else {
                    cell.setStatus(Status.empty);
                }

                canvasPanel.invalidate();
                canvasPanel.repaint();
            }
        });
    }

    public boolean hasGridSurface() {
        return gridSurface != null;
    }

    public void updateGridSurface(GridSurface gridSurface) {
        this.gridSurface = gridSurface;
        canvasPanel.updateCanvas(this.gridSurface);
    }

    public void findShortestPathDijkstras(int animationDelay) {
        findShortestPath(animationDelay, gridSurface.cloneGraph().dijkstrasAlgorithm());
    }

    public void findShortestPathAStar(int animationDelay, int costFunction) {
        HeuristicCost<Cell> heuristicCost;
        switch (costFunction) {
            case 0:
                heuristicCost = Cell.octileDistance;
                break;
            case 1:
                heuristicCost = Cell.euclideanDistance;
                break;
            case 2:
                heuristicCost = Cell.manhattanDistance;
                break;
            default:
                throw new IllegalStateException("Bad cost function index: " + costFunction);
        }

        findShortestPath(animationDelay, gridSurface.cloneGraph().aStarAlgorithm(heuristicCost));
    }

    public void findShortestPathBellmanFord(int animationDelay) {
        findShortestPath(animationDelay, gridSurface.cloneGraph().bellmanFordAlgorithm());
    }

    private void findShortestPath(int animationDelay, SearchAlgorithm<Cell> searchAlgorithm) {
        final WeightedGraph<Cell> graph = gridSurface.getGraph();
        Cell startCell = findStartCell(graph).orElseThrow(() -> new IllegalStateException("Start node missing!"));
        Cell goalCell = findGoalCell(graph).orElseThrow(() -> new IllegalStateException("Target node missing!"));

        searchAlgorithm.addFringeListener(new FringeListener<Cell>() {
            @Override
            public long simulationDelay() {
                return animationDelay;
            }

            @Override
            public void nodeVisited(Node<Cell> node) {
                Cell cell = node.getValue();
                if (cell.getStatus().equals(Status.closed)) {
                    throw new IllegalStateException();
                }

                cell.setCosts(node.getG(), node.getH());

                if (!cell.getStatus().isTerminal()) {
                    cell.setStatus(Status.visited);
                }

                canvasPanel.invalidate();
                canvasPanel.repaint();
            }

            @Override
            public void nodeClosed(Node<Cell> node) {
                Cell cell = node.getValue();

                if (!cell.getStatus().isTerminal()) {
                    cell.setStatus(Status.closed);
                }

                canvasPanel.invalidate();
                canvasPanel.repaint();
            }
        });

        statusLabel.setText("Searching path...");

        this.currentWorker = new SwingWorker<List<Cell>, Cell>() {
            long startTime;

            @Override
            protected List<Cell> doInBackground() {
                this.startTime = System.currentTimeMillis();
                return searchAlgorithm.findShortestPath(startCell, goalCell);
            }

            @Override
            protected void done() {
                try {
                    List<Cell> shortestPath = get();

                    long timeCost = System.currentTimeMillis() - startTime;

                    int pathCost = 0;
                    for (Cell cell : shortestPath) {
                        pathCost += cell.getGcost();
                        graph.node(cell).setStatus(Status.path);
                    }

                    graph.node(startCell).setStatus(Status.start);
                    graph.node(goalCell).setStatus(Status.goal);

                    statusLabel.setText(
                            String.format("Nodes: %d | Edges: %d | Path Nodes: %d | Path Cost: %d | Time: %d ms",
                                    graph.nodes().size(),
                                    graph.edges().size(),
                                    shortestPath.size(),
                                    pathCost,
                                    timeCost
                            )
                    );

                    canvasPanel.invalidate();
                    canvasPanel.repaint();
                } catch (CancellationException | InterruptedException | ExecutionException e) {
                    statusLabel.setText("Cancelled: " + e);
                    e.printStackTrace();
                }
            }
        };
        this.currentWorker.execute();
    }

    private Optional<Cell> findStartCell(WeightedGraph<Cell> graph) {
        return graph.nodes()
                .stream()
                .filter(cell -> cell.getStatus() == Status.start)
                .findFirst();
    }

    private Optional<Cell> findGoalCell(WeightedGraph<Cell> graph) {
        return graph.nodes()
                .stream()
                .filter(cell -> cell.getStatus() == Status.goal)
                .findFirst();
    }

    public void addObstacles() {
        gridSurface.getGraph().forEach(cell -> {
            if (cell.anyStatus(Status.path, Status.empty, Status.closed) && Math.random() > 0.95) {
                cell.setStatus(Status.obstacle);
            }
        });
    }

    public void cancelSearch() {
        if (this.currentWorker != null) {
            this.currentWorker.cancel(true);
            this.currentWorker = null;
        }
    }
}

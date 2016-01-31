
import java.util.ArrayList;
import java.util.Random;

public class MazeGen {
  private int dimensionX, dimensionY; // dimension of maze
  private int gridDimensionX, gridDimensionY; // dimension of output grid
  public int [][] grid; // output grid
  private Cell[][] cells; // 2d array of Cells
  private Random random = new Random(); // The random object

  // initialize with x and y the same
  public MazeGen(int aDimension) {
      // Call the constructor with X and Y the same
      this(aDimension, aDimension);
  }
  // constructor
  public MazeGen(int xDimension, int yDimension) {
      dimensionX = xDimension; //the maze dimensions
      dimensionY = yDimension; //the maze dimensions
      gridDimensionX = xDimension * 4 + 1; //the wall dimensions
      gridDimensionY = yDimension * 2 + 1; //the wall dimensions
      grid = new int[gridDimensionX][gridDimensionY]; //create the maze grid
      init(); //initialize the cells
      generateMaze(); //generate the maze
      updateGrid(); //create the integer array
  }

  private void init() {
      // create cells
      cells = new Cell[dimensionX][dimensionY];
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              cells[x][y] = new Cell(x, y, false); // create cell (see Cell constructor)
          }
      }
  }

  // inner class to represent a cell
  private class Cell {
    int x, y; // coordinates
    // cells this cell is connected to
    ArrayList<Cell> neighbors = new ArrayList<>();
    // impassable cell
    boolean wall = true;
    // if true, has yet to be used in generation
    boolean open = true;
    // construct Cell at x, y
    Cell(int x, int y) {
        this(x, y, true);
    }
    // construct Cell at x, y and with whether it isWall
    Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.wall = isWall;
    }
    // add a neighbor to this cell, and this cell as a neighbor to the other
    void addNeighbor(Cell other) {
        if (!this.neighbors.contains(other)) { // avoid duplicates
            this.neighbors.add(other);
        }
        if (!other.neighbors.contains(this)) { // avoid duplicates
            other.neighbors.add(this);
        }
    }
    // used in updateGrid()
    boolean isCellBelowNeighbor() {
        return this.neighbors.contains(new Cell(this.x, this.y + 1));
    }
    // used in updateGrid()
    boolean isCellRightNeighbor() {
        return this.neighbors.contains(new Cell(this.x + 1, this.y));
    }
    
    // Cell equivalence
    public boolean equals(Object other) {
        if (!(other instanceof Cell)) return false;
        Cell otherCell = (Cell) other;
        return (this.x == otherCell.x && this.y == otherCell.y);
    }
    public int hashCode() {// Generate a random hash code
        return this.x + this.y * 256;
    }
  }
  // generate from top left
  private void generateMaze() {
      generateMaze(0, 0);
  }
  // generate the maze from coordinates x, y
  private void generateMaze(int x, int y) {
      generateMaze(getCell(x, y)); // generate from Cell at coordinates
  }
  private void generateMaze(Cell startAt) {
      // don't generate from nonexistant
      if (startAt == null) return;
      startAt.open = false; // indicate cell closed for generation
      ArrayList<Cell> cells = new ArrayList<>();
      cells.add(startAt);

      while (!cells.isEmpty()) {
          Cell cell;
          // this is to reduce long twisting halls with short easy to detect branches
          if (random.nextInt(10)==0)
              cell = cells.remove(random.nextInt(cells.size()));
          else cell = cells.remove(cells.size() - 1);
          // for collection
          ArrayList<Cell> neighbors = new ArrayList<>();
          // cells that could potentially be neighbours
          Cell[] potentialNeighbors = new Cell[]{
              getCell(cell.x + 1, cell.y),
              getCell(cell.x, cell.y + 1),
              getCell(cell.x - 1, cell.y),
              getCell(cell.x, cell.y - 1)
          };
          for (Cell other : potentialNeighbors) {
              // skip if outside, is a wall or is not opened
              if (other==null || other.wall || !other.open) continue;
              neighbors.add(other);
          }
          if (neighbors.isEmpty()) continue;
          // get random cell
          Cell selected = neighbors.get(random.nextInt(neighbors.size()));
          // add as neighbor
          selected.open = false; // indicate cell closed for generation
          cell.addNeighbor(selected);
          cells.add(cell);
          cells.add(selected);
      }
  }
  // used to get a Cell at x, y; returns null out of bounds
  public Cell getCell(int x, int y) {
      try {
          return cells[x][y];
      } catch (ArrayIndexOutOfBoundsException e) { // catch out of bounds
          return null;
      }
  }
  
  public void updateGrid() {
      int backChar = 2, wallChar = 3, cellChar = 0;
      // fill background
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              grid[x][y] = backChar;
          }
      }
      // build walls
      for (int x = 0; x < gridDimensionX; x ++) {
          for (int y = 0; y < gridDimensionY; y ++) {
              if (x % 4 == 0 || y % 2 == 0)
                  grid[x][y] = wallChar;
          }
      }
      // make meaningful representation
      for (int x = 0; x < dimensionX; x++) {
          for (int y = 0; y < dimensionY; y++) {
              Cell current = getCell(x, y);
              int gridX = x * 4 + 2, gridY = y * 2 + 1;
        
                  grid[gridX][gridY] = cellChar;
                  if (current.isCellBelowNeighbor()) {
                      grid[gridX][gridY + 1] = cellChar;
                      grid[gridX + 1][gridY + 1] = backChar;
                      grid[gridX - 1][gridY + 1] = backChar;
                  }
                  if (current.isCellRightNeighbor()) {
                      grid[gridX + 2][gridY] = cellChar;
                      grid[gridX + 1][gridY] = cellChar;
                      grid[gridX + 3][gridY] = cellChar;
        
                  
              }
          }
      }
  }
  
}

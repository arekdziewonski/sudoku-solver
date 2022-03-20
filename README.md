# sudoku-solver
An application solving a sudoku puzzle using a graph database (embedded neo4j v4) as a data model. Uses the deprecated traversal framework that will be removed in v5.

The cells in the grid are represented by nodes in the graph. There are two kinds of relationships between them:
* `next` - used to sequentially traverse through all cells
* `adjacent` - used to check the correctness of the solution (connects cells which can't have the same value, i.e. positioned in the same row, column or box)

## Linting

```
gradle lint
```

## Formatting

```
gradle format
```

## Testing

```
gradle test -i
```

## Building

```
gradle build
```

## Running

```
rm -rf data  # drop the database
gradle run --args="<file-with-initial-grid>"
```

# sudoku-solver
An application solving a sudoku puzzle using a graph database as a data model.

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

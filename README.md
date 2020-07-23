Note 1:
  - There are preprocessing steps for indexing the data in the file
 
  Step 1: 
  - find offsets of all genes in the dna file

  Step 2: 
  - insert strings at offsets into the a prefix tree
  - any matching gene is associated to a valid path in the tree

Note 2: 
- Within the indexing period the server will return "Still indexing ..." with status 503
- Later on the server will quickly detect matched genes via prefix tree traversal
- For home assignment simplicity, local storage and local locks were used
- In practice the store should be on some external resource as described in the code detailed comments

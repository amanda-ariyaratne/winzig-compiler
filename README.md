#WinZig Compiler
###CS4542 - Compiler Design

`Index Number : 170043A`

`Name : K. H. A. A. Ariyaratne`


Github repository url : https://github.com/amanda-ariyaratne/winzig-compiler

To run the compiler, execute the following on the command line.

```
> tar xvf 170043A.tar
> make
> java winzigc –code winzig_test_programs/p1
```

Three sample programs are given inside the `winzig_test_programs` directory namely `p1`, `p2`, and `p3`.

The errors will be generated as a text file `errors.txt`.
The code for the stack machine will be generated as a text file `code.txt`.

Note: The code for the stack machine will only be generated in the absence of any errors. 

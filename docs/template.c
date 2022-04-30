/*******************************************************************
          Copyright (C) 1986 by Manuel E. Bermudez
          Translated to C - 1991
*******************************************************************/


#define LeftMode 0
#define RightMode 1

    /*  ABSTRACT MACHINE OPERATIONS  */
#define    NOP          1   /* 'NOP'       */
#define    HALTOP       2   /* 'HALT'      */
#define    LITOP        3   /* 'LIT'       */
#define    LLVOP        4   /* 'LLV'       */
#define    LGVOP        5   /* 'LGV'       */
#define    SLVOP        6   /* 'SLV'       */
#define    SGVOP        7   /* 'SGV'       */
#define    LLAOP        8   /* 'LLA'       */
#define    LGAOP        9   /* 'LGA'       */
#define    UOPOP       10   /* 'UOP'       */
#define    BOPOP       11   /* 'BOP'       */
#define    POPOP       12   /* 'POP'       */
#define    DUPOP       13   /* 'DUP'       */
#define    SWAPOP      14   /* 'SWAP'      */
#define    CALLOP      15   /* 'CALL'      */
#define    RTNOP       16   /* 'RTN'       */
#define    GOTOOP      17   /* 'GOTO'      */
#define    CONDOP      18   /* 'COND'      */
#define    CODEOP      19   /* 'CODE'      */
#define    SOSOP       20   /* 'SOS'       */
#define    LIMITOP     21   /* 'LIMIT'     */

    /* ABSTRACT MACHINE OPERANDS */

         /* UNARY OPERANDS */
#define    UNOT        22   /* 'UNOT'     */
#define    UNEG        23   /* 'UNEG'     */
#define    USUCC       24   /* 'USUCC'    */
#define    UPRED       25   /* 'UPRED'    */
         /* BINARY OPERANDS */
#define    BAND        26   /* 'BAND'     */
#define    BOR         27   /* 'BOR'      */
#define    BPLUS       28   /* 'BPLUS'    */
#define    BMINUS      29   /* 'BMINUS'   */
#define    BMULT       30   /* 'BMULT'    */
#define    BDIV        31   /* 'BDIV'     */
#define    BEXP        32   /* 'BEXP'     */
#define    BMOD        33   /* 'BMOD'     */
#define    BEQ         34   /* 'BEQ'      */
#define    BNE         35   /* 'BNE'      */
#define    BLE         36   /* 'BLE'      */
#define    BGE         37   /* 'BGE'      */
#define    BLT         38   /* 'BLT'      */
#define    BGT         39   /* 'BGT'      */
         /* OS SERVICE CALL OPERANDS */
#define    TRACEX      40   /* 'TRACEX'   */
#define    DUMPMEM     41   /* 'DUMPMEM'  */
#define    OSINPUT     42   /* 'INPUT'    */
#define    OSINPUTC    43   /* 'INPUT'    */
#define    OSOUTPUT    44   /* 'OUTPUT'   */
#define    OSOUTPUTC   45   /* 'OUTPUT'   */
#define    OSOUTPUTL   46   /* 'OUTPUTL'  */
#define    OSEOF       47   /* 'EOF'      */

         /* TREE NODE NAMES */
#define    ProgramNode  48   /* 'program'  */
#define    TypesNode    49   /* 'types'    */
#define    TypeNode     50   /* 'type'     */
#define    DclnsNode    51   /* 'dclns'    */
#define    VarNode      52   /* 'var'      */
#define    IntegerTNode 53   /* 'integer'  */
#define    BooleanTNode 54   /* 'boolean'  */
#define    BlockNode    55   /* 'block'    */
#define    AssignNode   56   /* 'assign'   */
#define    OutputNode   57   /* 'output'   */ 
#define    IfNode       58   /* 'if'       */
#define    WhileNode    59   /* 'while'    */
#define    RepeatNode   60   /* 'repeat'   */
#define    NullNode     61   /* '<null>'   */
#define    ANDNode      62   /* 'and'      */
#define    ORNode       63   /* 'or'       */
#define    LTNode       64   /* '<'        */
#define    LENode       65   /* '<='       */
#define    EQNode       66   /* '='        */
#define    NEQNode      67   /* '<>'       */
#define    GENode       68   /* '>='       */
#define    GTNode       69   /* '>'        */
#define    PlusNode     70   /* '+'        */
#define    MinusNode    71   /* '-'        */
#define    MultNode     72   /* '*'        */
#define    DivNode      73   /* '/'        */
#define    ModNode      74   /* 'mod'      */
#define    EXPNode      75   /* '**'       */
#define    NotNode      76   /* 'not'      */
#define    ReadNode     77   /* 'read'     */
#define    EOFNode      78   /* 'eof'      */
#define    IntegerNode  79   /* '<integer>'*/
#define    BooleanNode  80   /* '<boolean>'*/
#define    IdentifierNode 81 /* '<identifier>'*/
#define    ForUpNode      82 /* 'upfor'    */
#define    ForDownNode    83 /* 'downfor'  */ 
#define    SwapNode       84 /* 'swap'     */
#define    LoopNode       85 /* 'loop'     */
#define    ExitNode       86 /* 'exit'     */
#define    CaseNode       87 /* 'case'     */
#define    SwitchNode     88 /* 'switch'   */
#define    RangeNode      89 /* 'range'    */
#define    OtherwiseNode  90 /* 'otherwise'*/
#define    CharNode       91 /* '<char>'   */
#define    StringNode     92 /* '<string>' */
#define    SuccNode       93 /* 'succ'     */
#define    PredNode       94 /* 'pred'     */
#define    OrdNode        95 /* 'ord'      */
#define    ChrNode        96 /* 'chr'      */
#define    ConstsNode     97 /* 'consts'   */
#define    ConstNode      98 /* 'const'    */
#define    LitNode        99 /* 'lit'      */
#define    CharTNode     100 /* 'char'     */
#define    FunctionNode  101 /* 'fnc'      */
#define    ProcedureNode 102 /* 'proc'     */
#define    CallNode      103 /* 'call'     */
#define    ReturnNode    104 /* 'return'   */
#define    SubProgsNode  105 /* 'subprogs' */
#define    ParamsNode    106 /* 'params'   */

#define    NumberOfNodes 106 /* '<identifier>'*/
typedef int Mode;

FILE *CodeFile;
char *CodeFileName;
Clabel HaltLabel;

char *mach_op[] = 
    {"NOP","HALT","LIT","LLV","LGV","SLV","SGV","LLA","LGA",
     "UOP","BOP","POP","DUP","SWAP","CALL","RTN","GOTO","COND",
     "CODE","SOS","LIMIT","UNOT","UNEG","USUCC","UPRED","BAND",
     "BOR","BPLUS","BMINUS","BMULT","BDIV","BEXP","BMOD","BEQ",
     "BNE","BLE","BGE","BLT","BGT","TRACEX","DUMPMEM","INPUT",
     "INPUTC","OUTPUT","OUTPUTC","OUTPUTL","EOF"};

/****************************************************************** 
   add new node names to the end of the array, keeping in strict order
   as defined above, then adjust the j loop control variable in
   InitializeNodeNames(). 
*******************************************************************/
char *node_name[] =
    {"program","types","type","dclns","var","integer",
     "boolean","block","assign","output","if","while","repeat",
     "<null>","and","or","<","<=","=","<>",">=",">","+",
     "-","*","/","mod","**","not","read","eof","<integer>","<boolean>",
     "<identifier>","upfor","downfor","swap","loop","exit","case","switch",
     "range", "otherwise", "<char>", "<string>", "succ", "pred", "ord", "chr",
     "consts", "const", "lit", "char", "fnc", "proc", "call", "return", "subprogs", "params"  };


void CodeGenerate(int argc, char *argv[])
{
  
  
}


void InitializeCodeGenerator(int argc,char *argv[])
{
   InitializeMachineOperations();
   InitializeNodeNames();
   FrameSize = 0;
   CurrentProcLevel = 0;
   LabelCount = 0;
   CodeFileName = System_Argument("-code", "_CODE", argc, argv); 
}


void InitializeMachineOperations(void)
{
  int i,j;

   for (i=0, j=1; i < 47; i++, j++)
      String_Array_To_String_Constant (mach_op[i],j);
}



void InitializeNodeNames(void)
{
   int i,j;

   for (i=0, j=48; j <= NumberOfNodes; i++, j++)
      String_Array_To_String_Constant (node_name[i],j);
}



String MakeStringOf(int Number)
{
   Stack Temp;

   Temp = AllocateStack (50);
   ResetBufferInTextTable();
   if (Number == 0)
      AdvanceOnCharacter ('0');
   else
   {
      while (Number > 0)
      {
         Push (Temp,(Number % 10) + 48);
         Number /= 10;
      }

      while ( !(IsEmpty (Temp)))
         AdvanceOnCharacter ((char)(Pop(Temp)));
   }   
   return (ConvertStringInBuffer()); 
}  




int NKids (TreeNode T)
{
   return (Rank(T));
}


void Expression (TreeNode T, Clabel CurrLabel)
{
   int Kid;
   Clabel Label1;

   if (TraceSpecified)
   {
      fprintf (TraceFile, "<<< CODE GENERATOR >>> Processing Node ");
      Write_String (TraceFile, NodeName (T) );
      fprintf (TraceFile, " , Label is  ");
      Write_String (TraceFile, CurrLabel);
      fprintf (TraceFile, "\n");
   }

   switch (NodeName(T))
   {
      case ANDNode :
         Expression ( Child(T,1) , CurrLabel);
         Expression ( Child(T,2) , NoLabel);
         CodeGen1 (BOPOP, BAND, NoLabel);
         DecrementFrameSize();
         break;
      case ORNode :
         Expression ( Child(T,1) , CurrLabel);
         Expression ( Child(T,2) , NoLabel);
         CodeGen1 (BOPOP, BOR, NoLabel);
         DecrementFrameSize();
         break;
      case LTNode :
        

      default :
         ReportTreeErrorAt(T);
         printf ("<<< CODE GENERATOR >>k> : UNKNOWN NODE NAME ");
         Write_String (stdout,NodeName(T));
         printf ("\n");

   } /* end switch */
} /* end Expression */




## Let Language Grammar Rules

expr &rarr; **let** id **:=** expr **in** expr | term { ( **+** | **-** ) term }

term &rarr; factor { ( <strong>*</strong> | **/** ) factor }

factor &rarr; id | num | **(** expr **)**

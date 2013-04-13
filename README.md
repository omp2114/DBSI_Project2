Tasks:
1. Create subsets nodes class
2. Create plans class
3. Create the subsets nodes array. Make sure it is ordered in increasing order, with the branching term initialized to 0.
4. Part 1 of algorithm



Definitions:
fcost(E) = kr + (k − 1)l + f 1 + · · · + f k + t.





Example 4.4. Consider Algorithm No-Branch on k basic terms. The total
cost for each iteration is kr + (k − 1)l + f 1 + · · · + f k + a.
/* Algorithm No-Branch */
for(i=0;i<number_of_records;i++) {
answer[j] = i;
j += (f1(r1[i]) & ... & fk(rk[i]));
}


Example 4.5. Consider Algorithm Logical-And on k basic terms, with
selectivities p1 , . . . , pk . The total cost for each iteration is kr + (k − 1)l +
f 1 + · · · + f k + t + mq + p1 · · · pk a, where q = p1 · · · pk if p1 · · · pk ≤ 0.5 and
q = 1 − p1 · · · pk otherwise. The q term describes the branch prediction be-
havior: we assume the system predicts the branch to the next iteration will be
taken exactly when p1 · · · pk ≤ 0.5.

/* Algorithm Logical-And */
for(i=0;i<number_of_records;i++) {
if(f1(r1[i]) & ... & fk(rk[i]))
{answer[j++] = i;}
}

/* Algorithm Branching-And */
for(i=0;i<number_of_records;i++) {
if(f1(r1[i]) && ... && fk(rk[i]))
{answer[j++] = i;}
}


5. Part 2 of algorithm
6. Final recursive step
7. Output code generation
Playfair cipher<br>
J is omitted.<br>
if msg is odd len, 'Z' is appended to the message.<br><br>

To compile:<br>
1.) navigate to the src folder within the project<br><br>
2.) compile: javac Playfair.java<br>

To run:<br>
1.) java Playfair [-e/-d] [keyword] [message]<br>
e.g java Playfair -e mykey ALICE<br>
<<Output>><br>
Encrypt mode.<br>
Input'd Keyword: MYKEY<br>
MYKEA<br>
BCDFG<br>
HILNO<br>
PQRST<br>
UVWXZ<br>
Adjusted PlainText: ALICEZ<br>
Adjusted keyword: MYKE<br>
Encrypted Text: OKQIXA<br>
<<Output End>><br>


e.g java Playfair -d mykey OKQIXA<br><br>
<<Output>><br>
Decrypt mode.<br>
Input'd Keyword: MYKEY<br>
MYKEA<br>
BCDFG<br>
HILNO<br>
PQRST<br>
UVWXZ<br>
Decrypted Text: ALICEZ<br>
<<Output End>><br>

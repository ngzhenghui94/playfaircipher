Playfair cipher
J is omitted.
if msg is odd len, 'Z' is appended to the message.

To compile:
1.) navigate to the src folder within the project
2.) compile: javac Playfair.java

To run:
1.) java Playfair [-e/-d] [keyword] [message]
e.g java Playfair -e mykey ALICE
<<Output>>
Encrypt mode.
Input'd Keyword: MYKEY
MYKEA
BCDFG
HILNO
PQRST
UVWXZ
Adjusted PlainText: ALICEZ
Adjusted keyword: MYKE
Encrypted Text: OKQIXA
<<Output End>>


e.g java Playfair -d mykey OKQIXA
<<Output>>
Decrypt mode.
Input'd Keyword: MYKEY
MYKEA
BCDFG
HILNO
PQRST
UVWXZ
Decrypted Text: ALICEZ
<<Output End>>
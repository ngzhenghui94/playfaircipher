Playfair cipher
// Adjust the plainText by replacing I with J and removes all spaces.
// Text with consecutive same char, will have x inserted inbetween
// If plainText len is odd, append 'Z' to it. (https://learncryptography.com/classical-encryption/playfair-cipher/)
// Therefore: If MSG = "zzzz" Adjusted MSG = "zxzxzxz" (ODD length) = "zxzxzxzz"

To compile:
1.) navigate to the src folder within the project
2.) compile: javac Playfair.java

To run:
1.) java Playfair [-e/-d] [keyword] [message]
e.g java Playfair -e test zzzz
<<Output>>
Encrypt Mode.
Input'd Keyword: TEST
TESAB
CDFGH
JKLMN
OPQRU
VWXYZ
Adjusted PlainText: ZXZXZXZZ
Adjusted keyword: TES
Encrypted Text: VYVYVYVV
<<Output End>>


e.g java Playfair -d mykey OKQIXA
<<Output>>
Decrypt mode.
Input'd Keyword: TEST
TESAB
CDFGH
JKLMN
OPQRU
VWXYZ
Decrypted Text: ZXZXZXZZ
<<Output End>>
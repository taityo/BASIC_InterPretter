package newlang5;

public enum LexicalType {
	LITERAL,	// ������萔�@�i��F�@�g������h�j
	INTVAL,		// �����萔	�i��F�@�R�j
	DOUBLEVAL,	// �����_�萔	�i��F�@�P�D�Q�j
	NAME,		// �ϐ�		�i��F�@i�j
	IF,			// IF
	THEN,		// THEN
	ELSE,		// ELSE
	ELSEIF,		// ELSEIF
	ENDIF,		// ENDIF
	FOR,		// FOR
	FORALL,		// FORALL
	STEP,
	NEXT	,	// NEXT
	EQ,			// =
	LT,			// <
	GT,			// >
	LE,			// <=, =<
	GE,			// >=, =>
	NE,			// <>
	FUNC,		// SUB
	DIM,		// DIM
	AS,			// AS
	END,		// END
	NL,			// ���s
	DOT,		// .
	WHILE,		// WHILE
	DO,			// DO
	UNTIL,		// UNTIL
	ADD,		// +
	SUB,		// -
	MUL,		// *
	DIV,		// /
	LP,			// )
	RP,			// (
	COMMA,		// ,
	LOOP,		// LOOP
	TO,			// TO
	WEND,		// WEND
	BOOLVAL,	// TRUE, FALSE
	EOF,		// end of file
}

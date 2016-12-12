// Generated from C:/Users/Jeff/Academic/Compiler-Contruction/phase-1/src/main/java\MiniJava.g4 by ANTLR 4.5.3
package com.compiler.generated.antlr;


import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiniJavaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, INT=38, BOOL=39, 
		ID=40, WS=41, COMMENT=42, LINE_COMMENT=43;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
		"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
		"T__33", "T__34", "T__35", "T__36", "INT", "BOOL", "ID", "WS", "COMMENT", 
		"LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'class'", "'extends'", "'{'", "'}'", "'public'", "'static'", "'void'", 
		"'main'", "'('", "'String'", "'['", "']'", "')'", "';'", "','", "'if'", 
		"'else'", "'while'", "'System.out.println'", "'='", "'return'", "'recur'", 
		"'?'", "':'", "'.'", "'length'", "'-'", "'!'", "'new'", "'int'", "'+'", 
		"'*'", "'<'", "'&&'", "'this'", "'boolean'", "'mutable'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "INT", "BOOL", "ID", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MiniJavaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MiniJava.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2-\u0139\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3"+
		" \3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&"+
		"\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\7\'\u0101\n\'\f\'\16\'\u0104\13\'\5\'\u0106"+
		"\n\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\5(\u0111\n(\3)\3)\7)\u0115\n)\f)\16)\u0118"+
		"\13)\3*\6*\u011b\n*\r*\16*\u011c\3*\3*\3+\3+\3+\3+\7+\u0125\n+\f+\16+"+
		"\u0128\13+\3+\3+\3+\3+\3+\3,\3,\3,\3,\7,\u0133\n,\f,\16,\u0136\13,\3,"+
		"\3,\3\u0126\2-\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-\3\2\b\3\2\63;\3\2\62;\5\2"+
		"C\\aac|\6\2\62;C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\u013f\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"+
		"\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2"+
		"=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3"+
		"\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2"+
		"\2\2W\3\2\2\2\3Y\3\2\2\2\5_\3\2\2\2\7g\3\2\2\2\ti\3\2\2\2\13k\3\2\2\2"+
		"\rr\3\2\2\2\17y\3\2\2\2\21~\3\2\2\2\23\u0083\3\2\2\2\25\u0085\3\2\2\2"+
		"\27\u008c\3\2\2\2\31\u008e\3\2\2\2\33\u0090\3\2\2\2\35\u0092\3\2\2\2\37"+
		"\u0094\3\2\2\2!\u0096\3\2\2\2#\u0099\3\2\2\2%\u009e\3\2\2\2\'\u00a4\3"+
		"\2\2\2)\u00b7\3\2\2\2+\u00b9\3\2\2\2-\u00c0\3\2\2\2/\u00c6\3\2\2\2\61"+
		"\u00c8\3\2\2\2\63\u00ca\3\2\2\2\65\u00cc\3\2\2\2\67\u00d3\3\2\2\29\u00d5"+
		"\3\2\2\2;\u00d7\3\2\2\2=\u00db\3\2\2\2?\u00df\3\2\2\2A\u00e1\3\2\2\2C"+
		"\u00e3\3\2\2\2E\u00e5\3\2\2\2G\u00e8\3\2\2\2I\u00ed\3\2\2\2K\u00f5\3\2"+
		"\2\2M\u0105\3\2\2\2O\u0110\3\2\2\2Q\u0112\3\2\2\2S\u011a\3\2\2\2U\u0120"+
		"\3\2\2\2W\u012e\3\2\2\2YZ\7e\2\2Z[\7n\2\2[\\\7c\2\2\\]\7u\2\2]^\7u\2\2"+
		"^\4\3\2\2\2_`\7g\2\2`a\7z\2\2ab\7v\2\2bc\7g\2\2cd\7p\2\2de\7f\2\2ef\7"+
		"u\2\2f\6\3\2\2\2gh\7}\2\2h\b\3\2\2\2ij\7\177\2\2j\n\3\2\2\2kl\7r\2\2l"+
		"m\7w\2\2mn\7d\2\2no\7n\2\2op\7k\2\2pq\7e\2\2q\f\3\2\2\2rs\7u\2\2st\7v"+
		"\2\2tu\7c\2\2uv\7v\2\2vw\7k\2\2wx\7e\2\2x\16\3\2\2\2yz\7x\2\2z{\7q\2\2"+
		"{|\7k\2\2|}\7f\2\2}\20\3\2\2\2~\177\7o\2\2\177\u0080\7c\2\2\u0080\u0081"+
		"\7k\2\2\u0081\u0082\7p\2\2\u0082\22\3\2\2\2\u0083\u0084\7*\2\2\u0084\24"+
		"\3\2\2\2\u0085\u0086\7U\2\2\u0086\u0087\7v\2\2\u0087\u0088\7t\2\2\u0088"+
		"\u0089\7k\2\2\u0089\u008a\7p\2\2\u008a\u008b\7i\2\2\u008b\26\3\2\2\2\u008c"+
		"\u008d\7]\2\2\u008d\30\3\2\2\2\u008e\u008f\7_\2\2\u008f\32\3\2\2\2\u0090"+
		"\u0091\7+\2\2\u0091\34\3\2\2\2\u0092\u0093\7=\2\2\u0093\36\3\2\2\2\u0094"+
		"\u0095\7.\2\2\u0095 \3\2\2\2\u0096\u0097\7k\2\2\u0097\u0098\7h\2\2\u0098"+
		"\"\3\2\2\2\u0099\u009a\7g\2\2\u009a\u009b\7n\2\2\u009b\u009c\7u\2\2\u009c"+
		"\u009d\7g\2\2\u009d$\3\2\2\2\u009e\u009f\7y\2\2\u009f\u00a0\7j\2\2\u00a0"+
		"\u00a1\7k\2\2\u00a1\u00a2\7n\2\2\u00a2\u00a3\7g\2\2\u00a3&\3\2\2\2\u00a4"+
		"\u00a5\7U\2\2\u00a5\u00a6\7{\2\2\u00a6\u00a7\7u\2\2\u00a7\u00a8\7v\2\2"+
		"\u00a8\u00a9\7g\2\2\u00a9\u00aa\7o\2\2\u00aa\u00ab\7\60\2\2\u00ab\u00ac"+
		"\7q\2\2\u00ac\u00ad\7w\2\2\u00ad\u00ae\7v\2\2\u00ae\u00af\7\60\2\2\u00af"+
		"\u00b0\7r\2\2\u00b0\u00b1\7t\2\2\u00b1\u00b2\7k\2\2\u00b2\u00b3\7p\2\2"+
		"\u00b3\u00b4\7v\2\2\u00b4\u00b5\7n\2\2\u00b5\u00b6\7p\2\2\u00b6(\3\2\2"+
		"\2\u00b7\u00b8\7?\2\2\u00b8*\3\2\2\2\u00b9\u00ba\7t\2\2\u00ba\u00bb\7"+
		"g\2\2\u00bb\u00bc\7v\2\2\u00bc\u00bd\7w\2\2\u00bd\u00be\7t\2\2\u00be\u00bf"+
		"\7p\2\2\u00bf,\3\2\2\2\u00c0\u00c1\7t\2\2\u00c1\u00c2\7g\2\2\u00c2\u00c3"+
		"\7e\2\2\u00c3\u00c4\7w\2\2\u00c4\u00c5\7t\2\2\u00c5.\3\2\2\2\u00c6\u00c7"+
		"\7A\2\2\u00c7\60\3\2\2\2\u00c8\u00c9\7<\2\2\u00c9\62\3\2\2\2\u00ca\u00cb"+
		"\7\60\2\2\u00cb\64\3\2\2\2\u00cc\u00cd\7n\2\2\u00cd\u00ce\7g\2\2\u00ce"+
		"\u00cf\7p\2\2\u00cf\u00d0\7i\2\2\u00d0\u00d1\7v\2\2\u00d1\u00d2\7j\2\2"+
		"\u00d2\66\3\2\2\2\u00d3\u00d4\7/\2\2\u00d48\3\2\2\2\u00d5\u00d6\7#\2\2"+
		"\u00d6:\3\2\2\2\u00d7\u00d8\7p\2\2\u00d8\u00d9\7g\2\2\u00d9\u00da\7y\2"+
		"\2\u00da<\3\2\2\2\u00db\u00dc\7k\2\2\u00dc\u00dd\7p\2\2\u00dd\u00de\7"+
		"v\2\2\u00de>\3\2\2\2\u00df\u00e0\7-\2\2\u00e0@\3\2\2\2\u00e1\u00e2\7,"+
		"\2\2\u00e2B\3\2\2\2\u00e3\u00e4\7>\2\2\u00e4D\3\2\2\2\u00e5\u00e6\7(\2"+
		"\2\u00e6\u00e7\7(\2\2\u00e7F\3\2\2\2\u00e8\u00e9\7v\2\2\u00e9\u00ea\7"+
		"j\2\2\u00ea\u00eb\7k\2\2\u00eb\u00ec\7u\2\2\u00ecH\3\2\2\2\u00ed\u00ee"+
		"\7d\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7q\2\2\u00f0\u00f1\7n\2\2\u00f1"+
		"\u00f2\7g\2\2\u00f2\u00f3\7c\2\2\u00f3\u00f4\7p\2\2\u00f4J\3\2\2\2\u00f5"+
		"\u00f6\7o\2\2\u00f6\u00f7\7w\2\2\u00f7\u00f8\7v\2\2\u00f8\u00f9\7c\2\2"+
		"\u00f9\u00fa\7d\2\2\u00fa\u00fb\7n\2\2\u00fb\u00fc\7g\2\2\u00fcL\3\2\2"+
		"\2\u00fd\u0106\7\62\2\2\u00fe\u0102\t\2\2\2\u00ff\u0101\t\3\2\2\u0100"+
		"\u00ff\3\2\2\2\u0101\u0104\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2"+
		"\2\2\u0103\u0106\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u00fd\3\2\2\2\u0105"+
		"\u00fe\3\2\2\2\u0106N\3\2\2\2\u0107\u0108\7v\2\2\u0108\u0109\7t\2\2\u0109"+
		"\u010a\7w\2\2\u010a\u0111\7g\2\2\u010b\u010c\7h\2\2\u010c\u010d\7c\2\2"+
		"\u010d\u010e\7n\2\2\u010e\u010f\7u\2\2\u010f\u0111\7g\2\2\u0110\u0107"+
		"\3\2\2\2\u0110\u010b\3\2\2\2\u0111P\3\2\2\2\u0112\u0116\t\4\2\2\u0113"+
		"\u0115\t\5\2\2\u0114\u0113\3\2\2\2\u0115\u0118\3\2\2\2\u0116\u0114\3\2"+
		"\2\2\u0116\u0117\3\2\2\2\u0117R\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011b"+
		"\t\6\2\2\u011a\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011a\3\2\2\2\u011c"+
		"\u011d\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u011f\b*\2\2\u011fT\3\2\2\2\u0120"+
		"\u0121\7\61\2\2\u0121\u0122\7,\2\2\u0122\u0126\3\2\2\2\u0123\u0125\13"+
		"\2\2\2\u0124\u0123\3\2\2\2\u0125\u0128\3\2\2\2\u0126\u0127\3\2\2\2\u0126"+
		"\u0124\3\2\2\2\u0127\u0129\3\2\2\2\u0128\u0126\3\2\2\2\u0129\u012a\7,"+
		"\2\2\u012a\u012b\7\61\2\2\u012b\u012c\3\2\2\2\u012c\u012d\b+\2\2\u012d"+
		"V\3\2\2\2\u012e\u012f\7\61\2\2\u012f\u0130\7\61\2\2\u0130\u0134\3\2\2"+
		"\2\u0131\u0133\n\7\2\2\u0132\u0131\3\2\2\2\u0133\u0136\3\2\2\2\u0134\u0132"+
		"\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0137\3\2\2\2\u0136\u0134\3\2\2\2\u0137"+
		"\u0138\b,\2\2\u0138X\3\2\2\2\n\2\u0102\u0105\u0110\u0116\u011c\u0126\u0134"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
package AnalisadorLexico;

import java.util.ArrayList;

public class ParserSintat {
	boolean palavraVazia = true;
	public ParserSintat() {}

	public void parser(ArrayList<Token> arraytoken) {
		ArrayList<Token> arrayCompare = new ArrayList<Token>();
		ArrayList<Token> arrayListaComandos = new ArrayList<Token>();
		int aux = 0;
		if(arraytoken.get(0).getnome() == TipoToken.Delim) {
			System.out.println("Pass");
		}else{
			System.out.println("ERROR");
			System.exit(1);
		}
		if(arraytoken.get(1).getnome() == TipoToken.PCDec) {
			System.out.println("Pass");
		}else{
			System.out.println("ERROR");
			System.exit(1);
		}
		if(arraytoken.get(2).getnome() == TipoToken.Delim){
			System.out.println("ERROR");
			System.exit(1);
		}
		
		for(int i = 2; i < arraytoken.size(); i++) { 
			/////////////////LISTA DE DECLARACOES/////////////////////////////////////
			if(arraytoken.get(i).getnome() == TipoToken.Delim && arraytoken.get(i+1).getnome() == TipoToken.PCProg) {
				aux = i;
				for(int j = 2; j < aux; j++) 
					arrayCompare.add(arraytoken.get(j));
					//break;
			}			
		}
		if(aux +1 == arraytoken.size()) {
			System.out.println("ERROR");
			System.exit(1);
		}
		for(int i = aux+2; i < arraytoken.size(); i++) {
			arrayListaComandos.add(arraytoken.get(i));
		}
		if(listaDeclaracoes(arrayCompare) == false){
			System.out.println("ERROR");
			System.exit(1);
		}
		if(listaComandos(arrayListaComandos) == false) {
			System.out.println("ERROR");
			System.exit(1);
		}	
	}
	public boolean listaDeclaracoes(ArrayList<Token> arrayCompare) {
		ArrayList<Token> arrayDeclaracao = new ArrayList<Token>();
		boolean retorno = true;
		if(arrayCompare.isEmpty()) return true;
		for(int i = 0; i < 3; i++) {
			arrayDeclaracao.add(arrayCompare.get(0));
			arrayCompare.remove(0);
		}
		if(declaracao(arrayDeclaracao) == false) retorno = false;
		else listaDeclaracoes(arrayCompare);
		return retorno;
	}
	
	public boolean declaracao(ArrayList<Token> arrayDeclaracao) {
		boolean retorno = false;
		if(arrayDeclaracao.get(0).getnome() == TipoToken.Var) retorno = true;
		if(arrayDeclaracao.get(1).getnome() == TipoToken.Delim) retorno = true;
		if(arrayDeclaracao.get(2).getnome() == TipoToken.PCReal || arrayDeclaracao.get(2).getnome() == TipoToken.PCInt) retorno = true;
		return retorno;
	}
	
	public boolean listaComandos(ArrayList<Token> arrayListaComandos) {
		boolean retorno = true;
		if(arrayListaComandos.isEmpty()) return true;
		/*for(int i = 0; i < arrayListaComandos.size(); i++) 
			if(comando(arrayListaComandos) == false) return false;*/
		if(comando(arrayListaComandos) == false) return false;
		else listaComandos(arrayListaComandos);
		return retorno;
	}
	
	public boolean comando(ArrayList<Token> arrayListaComandos) {
		boolean retorno = true;
		if(comandoAtrib(arrayListaComandos) == false) return false;
		if(comandoEntrada(arrayListaComandos) == false) return false;
		if(comandoSaida(arrayListaComandos) == false) return false;
		return retorno;
	}
	
	public boolean comandoAtrib(ArrayList<Token> arrayListaComandos) {
		boolean retorno = false;
		int linha = arrayListaComandos.get(0).getlinha();
		ArrayList<Token> arrayListaExpressao = new ArrayList<Token>();	
		if(arrayListaComandos.get(0).getnome() == TipoToken.Var) {
			arrayListaComandos.remove(0);
			if(arrayListaComandos.get(0).getnome() == TipoToken.Atrib) {
				arrayListaComandos.remove(0);
				retorno = true;
			}else retorno = false;
		}else retorno = false;
		if(retorno == true) {
			for(int i = 0; i < arrayListaComandos.size(); i++) {
				if(linha == arrayListaComandos.get(i).getlinha()) {
					arrayListaExpressao.add(arrayListaComandos.get(i));
					arrayListaComandos.remove(i);
				}else return retorno;
			}
		}else return retorno;
		if(expressaoArit(arrayListaExpressao)) retorno = true;
		return retorno;
	}
	
	public boolean comandoEntrada(ArrayList<Token> arrayListaComandos) {
		boolean retorno = false;
		if(arrayListaComandos.get(0).getnome() == TipoToken.PCLer) {
			arrayListaComandos.remove(0);
			if(arrayListaComandos.get(0).getnome() == TipoToken.Var) {
				arrayListaComandos.remove(0);
				retorno = true;
			}
		}
		return retorno;
	}
	
	public boolean comandoSaida(ArrayList<Token> arrayListaComandos) {
		boolean retorno = false;
		if(arrayListaComandos.get(0).getnome() == TipoToken.PCImprimir) {
			arrayListaComandos.remove(0);
			if(arrayListaComandos.get(0).getnome() == TipoToken.Var || arrayListaComandos.get(0).getnome() == TipoToken.Cadeia) {
				arrayListaComandos.remove(0);
				retorno = true;
			}
		}
		return retorno;
	}
	
	public boolean expressaoArit(ArrayList<Token> arrayListaExpressao) {
		boolean retorno = false;
		if(arrayListaExpressao.isEmpty()) return true;
		if(termoArit(arrayListaExpressao)) 
			if(expressaoAritL(arrayListaExpressao)) retorno = true;
		return retorno;
	}
	
	public boolean termoArit(ArrayList<Token> arrayListaExpressao) {
		boolean retorno = false;
		if(arrayListaExpressao.isEmpty()) return true;
		if(fatorArit(arrayListaExpressao)) if(termoAritL(arrayListaExpressao));	
		return retorno;
	}
	
	public boolean fatorArit(ArrayList<Token> arrayListaExpressao) {
		boolean retorno = false;
		if(arrayListaExpressao.isEmpty()) return true;
		if(arrayListaExpressao.get(0).getnome() == TipoToken.Var) {
			arrayListaExpressao.remove(0);
			retorno = true;
		}else if (arrayListaExpressao.get(0).getnome() == TipoToken.NumInt) {
			arrayListaExpressao.remove(0);
			retorno = true;
		}else if(arrayListaExpressao.get(0).getnome() == TipoToken.AbrePar) {
			arrayListaExpressao.remove(0);
			if(expressaoArit(arrayListaExpressao)) {
				if(arrayListaExpressao.get(0).getnome() == TipoToken.FechaPar) {
					arrayListaExpressao.remove(0);
					retorno = true;
				}
			}
		}
		return retorno;
	}
	
	public boolean termoAritL(ArrayList<Token> arrayListaExpressao) {
		boolean retorno = false;
		if(arrayListaExpressao.isEmpty()) return true;
		if(arrayListaExpressao.get(0).getnome() == TipoToken.OpAritMult) {
			arrayListaExpressao.remove(0);
			if(fatorArit(arrayListaExpressao)) termoAritL(arrayListaExpressao);	
		}else if(arrayListaExpressao.get(0).getnome() == TipoToken.OpAritDiv) {
			arrayListaExpressao.remove(0);
			if(fatorArit(arrayListaExpressao)) termoAritL(arrayListaExpressao);
		}else return palavraVazia;
		return retorno;
	}
	
	public boolean expressaoAritL(ArrayList<Token> arrayListaExpressao) {
		boolean retorno = false;
		if(arrayListaExpressao.isEmpty()) return true;
		if(arrayListaExpressao.get(0).getnome() == TipoToken.OpAritSoma) {
			arrayListaExpressao.remove(0);
			if(termoArit(arrayListaExpressao)) expressaoAritL(arrayListaExpressao);
		}else if(arrayListaExpressao.get(0).getnome() == TipoToken.OpAritSub) {
			arrayListaExpressao.remove(0);
			if(termoArit(arrayListaExpressao)) expressaoAritL(arrayListaExpressao);
		}else return palavraVazia;
		return retorno;
	}
}
/**/
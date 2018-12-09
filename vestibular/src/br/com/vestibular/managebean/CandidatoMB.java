package br.com.vestibular.managebean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.vestibular.dao.DAO;
import br.com.vestibular.mensagens.Mensagem;
import br.com.vestibular.modelo.Candidato;
import br.com.vestibular.modelo.Curso;


@ManagedBean
@ViewScoped
public class CandidatoMB {
	
	private Candidato candidato;
	private List<Candidato> candidatos;
	private Integer codCurso;

	public CandidatoMB() {
		candidato = new Candidato();
		candidatos = new DAO<>(Candidato.class).listaTodos();
		
	}
	
	public void salvar() {
		DAO<Candidato> dao = new DAO<>(Candidato.class);
		
		if(candidato.getNumInscricao() == null) {
			// Obt�m o curso no BD pelo codigo forneciso no cadastro.
			Curso curso = new DAO<>(Curso.class).listaPorPK(codCurso);
			candidato.setCurso(curso);
			
			// Obt�m a nova inscri��o para o curso.
			Integer proxInscricao = curso.getCandidatos().size() + 1;
			curso.setTotalinscritos(curso.getCandidatos().size() + 1);
			String inscricao = String.format("%s-%05d", curso.getSiglacurso(), proxInscricao);
			candidato.setNumInscricao(inscricao);
			
			dao.adiciona(candidato);
			new DAO<>(Curso.class).altera(curso);
			
			Mensagem.msgInfo("Candidato cadastrado com sucesso!");
		}else {
			dao.altera(candidato);
			Mensagem.msgInfo("Candidato alterado com sucesso!");
		}
		
		candidato = new Candidato();
		candidatos = dao.listaTodos();
	}
	
	public void remover(Candidato candidato) {
		DAO<Candidato> dao = new DAO<>(Candidato.class);
		DAO<Curso> daoCurso = new DAO<>(Curso.class);
		Curso curso = daoCurso.listaPorPK(candidato.getCurso().getCodcurso());
		dao.remove(candidato);
		curso.setTotalinscritos(curso.getCandidatos().size());
		daoCurso.altera(curso);
		candidatos = dao.listaTodos();
		Mensagem.msgDelete("Candidato removido!");
	}

	public Candidato getCandidato() {
		return candidato;
	}

	public void setCandidato(Candidato candidato) {
		this.candidato = candidato;
	}

	public Integer getCodCurso() {
		return codCurso;
	}

	public void setCodCurso(Integer codCurso) {
		this.codCurso = codCurso;
	}

	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}
	
}

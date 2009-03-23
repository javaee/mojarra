package entities.sample;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import entities.Meeting;
import entities.Meetings;
import entities.Sprint;
import entities.Story;
import entities.Task;

public class SampleSprint extends Sprint{

	private static final long serialVersionUID = -8916240916230369184L;

	public SampleSprint(){
		this.setName("ScrumToys");
		this.setGoals("Permitir aos participantes da iteração identificar as estórias e as tarefas em andamento.");
		List<Task> tasks1 = new LinkedList<Task>();
		tasks1.add(new Task("Desenhar lista de estórias"));
		tasks1.add(new Task("Desenhar lista de estórias: abertas, em andamento e fechadas"));
		List<Task> tasks2 = new LinkedList<Task>();
		tasks2.add(new Task("Criar entidades Backlog e BacklogItem e respectivos serviços "));
		tasks2.add(new Task("Criar entidades Dashboard e Burndown e respectivos serviços"));
		tasks2.add(new Task("Criar entidades Meeting, Scrum Master, Product Owner, Team e Developer "));
		List<Task> tasks3 = new LinkedList<Task>();
		tasks3.add(new Task("Anotar propriedades das entidades persistentes"));
		tasks3.add(new Task("Anotar relacionamentoes e herança"));
		tasks3.add(new Task("Testar consultas do Dashboard e do Burndown"));
		tasks3.add(new Task("Melhorar desempenho das consultas"));
		List<Task> tasks4 = new LinkedList<Task>();
		tasks4.add(new Task("Criar dashboard.xhtml e respectivo DashboardMB"));
		tasks4.add(new Task("Criar burndown.xhtml e respectivo BurndownMB"));
		tasks4.add(new Task("Criar impediments.xhtml e respectivo ImpedimentsMB"));
		tasks4.add(new Task("Criar releases.xhtml e respectivo ReleasesMB"));
		tasks4.add(new Task("Criar product.xhtml e respectivo ProductMB"));
		this.getStories().add(new Story(tasks1, "Criar protótipo", "A interface de usuário deve apresentar estórias e tarefas, respeitando a prioridade e indicando o status", 100, 5));
		this.getStories().add(new Story(tasks2, "Criar entidades e serviços de negócio", "Os dados do protótipo devem ser estar representados como entidades de domínio e as operações como em serviços.", 200, 3));
		this.getStories().add(new Story(tasks3, "Criar integração com fonte de dados", "As entidades de negócio devem ser recuperadas ou manipuladas via serviços e estarem persistidas na fonte de dados escolhida.", 400, 3));
		this.getStories().add(new Story(tasks4, "Criar interface de usuário", "A interface de usuário deve usar os objetos criados nas estórias anteriores e a funcionalidade apta a ser expedida para o ambiente de produção", 300, 5));
		for (Story story: this.getStories()){
			story.setSprint(this);
		}//for
		this.setMeetings(new Meetings());
		this.getMeetings().setEstimation(new Meeting(2008,Calendar.NOVEMBER, 25));
		this.getMeetings().setSprintPlanningI(new Meeting(2008,Calendar.NOVEMBER, 26));
		this.getMeetings().setSprintPlanningII(new Meeting(2008,Calendar.NOVEMBER, 27));
		this.getMeetings().setRevision(new Meeting(2008,Calendar.DECEMBER, 15));
		this.getMeetings().setRetrospective(new Meeting(2008,Calendar.DECEMBER, 16));
		this.setDailyMeetingTime("09:00");	
	}
}

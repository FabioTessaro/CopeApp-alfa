
package com.copeapp.servlet.survey;

import java.io.IOException;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mapstruct.factory.Mappers;

import com.copeapp.dao.commons.UserDAO;
import com.copeapp.dao.survey.SurveyDao;
import com.copeapp.dto.survey.SurveyDTO;
import com.copeapp.dto.survey.SurveyRequestByIdDTO;
import com.copeapp.dto.survey.SurveyResponseByIdDTO;
import com.copeapp.entities.common.Role;
import com.copeapp.entities.common.User;
import com.copeapp.entities.survey.Survey;
import com.copeapp.mappers.survey.SurveyMapper;
import com.copeapp.tomcat9Misc.EntityManagerFactoryGlobal;
import com.copeapp.utilities.HttpStatusUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/rest/surveybyid")
public class SurveyById extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper objMap = new ObjectMapper();
		SurveyRequestByIdDTO surveyRequestById = objMap.readValue(request.getInputStream(), SurveyRequestByIdDTO.class);
		User currentUser = UserDAO.selectByBasicAuthTokenException(request.getHeader("Authorization"));

		Survey requiredSurvey = SurveyDao.getSurveyById(surveyRequestById.getSurveyId());

		ArrayList<Role> commonRole = new ArrayList<Role>(currentUser.getRoles());
		commonRole.retainAll(requiredSurvey.getSurveyViewersRoles());
		SurveyResponseByIdDTO responseDTO;
		if (!commonRole.isEmpty()) {
			responseDTO = new SurveyResponseByIdDTO(Mappers.getMapper(SurveyMapper.class).surveyToSurveyDTO(requiredSurvey));
			responseDTO.getSurveyDTO().setVoters(10); //TODO gestione dei votanti
			response.setStatus(HttpStatusUtility.OK);
		} else {
			responseDTO = new SurveyResponseByIdDTO(new SurveyDTO()); //TODO mando vuoto o non mando?
			response.setStatus(HttpStatusUtility.UNAUTHORIZED);
		}
		objMap.writeValue(response.getOutputStream(),responseDTO);

		/*try {
			SurveyDTO surveyDTO;
			ArrayList<RoleDTO> surveyViewersRoles = new ArrayList<RoleDTO>(); //create viewersRoles
			RoleDTO tmp1 = new RoleDTO();
			for (Role r : s.getSurveyViewersRoles()) {
				BeanUtils.copyProperties(tmp1, r);
				surveyViewersRoles.add(tmp1);
			}
			ArrayList<RoleDTO> commonRole = new ArrayList<RoleDTO>(surveyViewersRoles);
			commonRole.retainAll(currentUser.getRoles());
			if (!commonRole.isEmpty()) {
				ArrayList<RoleDTO> surveyVotersRoles = new ArrayList<RoleDTO>();	//create votersRoles		
				RoleDTO tmp = new RoleDTO();
				for (Role r : s.getSurveyVotersRoles()) {
					BeanUtils.copyProperties(tmp, r);
					surveyVotersRoles.add(tmp);
				}
				ArrayList<AnswerDTO> answers = new ArrayList<AnswerDTO>(); //create answer list
				AnswerDTO tmp2 = new AnswerDTO();
				for (Answer an : s.getAnswers()) {
					BeanUtils.copyProperties(tmp2, an);
					answers.add(tmp2);
				}
				User u = (User) s.getInsertUser();	//al posto che lo user mando il username
				String username = u.getUsername();
				surveyDTO = new SurveyDTO(s.getSurveyId(), s.getQuestion(), s.getCloseSurveyDate(), 10, surveyViewersRoles, surveyVotersRoles, username , s.getAnswersNumber(), answers);	
			} else {
				response.setStatus(HttpStatusUtility.UNAUTHORIZED);
				surveyDTO = new SurveyDTO();	//TODO gestione della non visibilità
			}
			objMap.writeValue(response.getOutputStream(), new SurveyResponseByIdDTO(surveyDTO));
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}*/

	}
}


package com.revature.contract;

import io.javalin.Javalin;
import io.javalin.http.HttpCode;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.Map;

import com.revature.contract.data.DAOFactory;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.Score;
import com.revature.contract.services.ScoreService;
import com.revature.contract.services.ScoreServiceImpl;
import com.revature.contract.services.UserService;
import com.revature.contract.services.UserServiceImpl;

public class ContractApp {
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
			config.enableCorsForAllOrigins();
		});

		app.start();
		
		ScoreService scoreServ = new ScoreServiceImpl(DAOFactory.getAssociateDAO(), DAOFactory.getScoreDAO());
		UserService userServ = new UserServiceImpl(DAOFactory.getAssociateDAO(), DAOFactory.getRubricDAO());

		app.routes(() -> {
			path("users", () -> {
				post(ctx -> {
					Associate associate = ctx.bodyAsClass(Associate.class);
					userServ.createUser(associate);
					ctx.json(associate);
				});
				path("code", () -> {
					post(ctx -> {
						Map<String, String> credentials = ctx.bodyAsClass(Map.class);
						Associate associate = userServ.logIn(credentials.get("firstName"), credentials.get("lastName"),
								credentials.get("code"));
						if (associate != null) {
							ctx.json(associate);
						} else {
							ctx.status(HttpCode.UNAUTHORIZED);
						}
					});
				});
			});
			path("scores/{associateId}", () -> {
				get(ctx -> {
					try {
						int associateId = Integer.valueOf(ctx.pathParam("associateId"));
						Map<String, Double> scores = scoreServ.viewScoresByAssociateId(associateId);
						if (scores != null)
							ctx.json(scores);
						else
							ctx.status(HttpCode.NOT_FOUND);
					} catch (NumberFormatException e) {
						ctx.status(HttpCode.BAD_REQUEST);
					}
				});
				post(ctx -> {
					try {
						int associateId = Integer.valueOf(ctx.pathParam("associateId"));
						Score score = ctx.bodyAsClass(Score.class);
						Associate associate = scoreServ.submitScore(score, associateId);

						if (associate != null)
							ctx.json(associate);
						else
							ctx.status(HttpCode.NOT_FOUND);
					} catch (NumberFormatException e) {
						ctx.status(HttpCode.BAD_REQUEST);
					}
				});
			});
			path("rubrics", () -> {
				get(ctx -> {
					ctx.json(userServ.getRubricThemes());
				});
				path("{associateId}", () -> {
					post(ctx -> {
						try {
							int associateId = Integer.valueOf(ctx.pathParam("associateId"));
							Rubric rubric = ctx.bodyAsClass(Rubric.class);
							Associate associate = userServ.submitRubric(rubric, associateId);

							if (associate != null)
								ctx.json(associate);
							else
								ctx.status(HttpCode.NOT_FOUND);
						} catch (NumberFormatException e) {
							ctx.status(HttpCode.BAD_REQUEST);
						}
					});
				});
			});
		});
	}
}

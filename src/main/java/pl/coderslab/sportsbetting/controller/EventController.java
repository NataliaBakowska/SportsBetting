package pl.coderslab.sportsbetting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.coderslab.sportsbetting.entity.Game;
import pl.coderslab.sportsbetting.comparator.GameComparator;
import pl.coderslab.sportsbetting.entity.Horse;
import pl.coderslab.sportsbetting.entity.Result;
import pl.coderslab.sportsbetting.service.FakerService;
import pl.coderslab.sportsbetting.service.GameServiceImpl;
import pl.coderslab.sportsbetting.service.HorseServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class EventController {
    @Autowired
    FakerService fakerService;

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    HorseServiceImpl horseService;

    Random rand = new Random();

    @GetMapping("/upcomingEvents")
    String showUpcomingEvents(Model model){
        List<Game> games= gameService.findAllFutureGames();
        games.sort(new GameComparator());
        model.addAttribute("game", games);
        return "events";
    }

    @GetMapping("/eventDetails/{id}")
    String showEventDetails(@PathVariable Long id, Model model){
        loadEvents(id, model);
        return "eventDetails";
    }

    @GetMapping("/currentDetails/{id}")
    String showCurrentEventDetails(@PathVariable Long id, Model model){
        loadEvents(id, model);
        return "eventDetailsCurrent";
    }

    private void loadEvents(@PathVariable Long id, Model model) {
        Game game = gameService.findEventById(id);
        model.addAttribute("game", game);
        List<Result> results = game.getResults();
        List<Horse> horses = new ArrayList<>();
        for (Result r : results) {
            horses.add(r.getHorse());
        }
        model.addAttribute("horses", horses);
    }

    @GetMapping("/pastEvents")
    String showPastEvents(Model model){
        List<Game> games = gameService.findAllPastGames();
        model.addAttribute("game",games);
        return "pastEvents";
    }

    @GetMapping("/end/{id}")
    String endGame(@PathVariable Long id, Model model){
        Game game = gameService.findEventById(id);
        game.setFinishingAt(org.joda.time.LocalDateTime.now().minusSeconds(2).toDate());
        gameService.saveGame(game);
        return "redirect:/home";
    }

    @GetMapping("/generateUpcoming")
    String generateUpcoming(Model model){
        fakerService.generateGameToBegin();
        return "redirect:/home";
    }
}

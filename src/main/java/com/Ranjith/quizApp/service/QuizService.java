package com.Ranjith.quizApp.service;

import com.Ranjith.quizApp.dao.QuestionDAO;
import com.Ranjith.quizApp.dao.QuizDAO;
import com.Ranjith.quizApp.model.Question;
import com.Ranjith.quizApp.model.QuestionWrapper;
import com.Ranjith.quizApp.model.Quiz;
import com.Ranjith.quizApp.model.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDAO quizDao;
    @Autowired
    QuestionDAO questionDao;

    public ResponseEntity<String> createQuiz(String category,int numQ,String title) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category,numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
       Optional<Quiz> quiz =  quizDao.findById(id);
       List<Question> questionsFromdb = quiz.get().getQuestions();
       List<QuestionWrapper> questionForUser = new ArrayList<>();
       for(Question q : questionsFromdb) {
           QuestionWrapper qw = new QuestionWrapper(q.getId(),q.getLevel(),q.getQuestion(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
           questionForUser.add(qw);
       }
       return new ResponseEntity<>(questionForUser,HttpStatus.OK);
    }

    public ResponseEntity<Integer> result(Integer id, List<Responses> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> q = quiz.getQuestions();

        int score = 0;
        int i =0;
        for(Responses r : responses) {
            if(r.getResponse().equals(q.get(i).getAnswer()))
                score += 1;
            i++;
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}

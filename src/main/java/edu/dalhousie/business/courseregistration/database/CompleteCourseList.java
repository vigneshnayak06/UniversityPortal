package edu.dalhousie.business.courseregistration.database;

import edu.dalhousie.business.courseregistration.constants.CourseRegistrationConstants;
import edu.dalhousie.business.courseregistration.controller.CourseRegistrationFactory;
import edu.dalhousie.business.courseregistration.model.Course;
import edu.dalhousie.business.courseregistration.model.ICourses;
import edu.dalhousie.database.IDatabaseConnection;
import edu.dalhousie.database.DatabaseConnectivity;
import edu.dalhousie.logger.ILogger;
import edu.dalhousie.logger.LoggerAbstractFactory;
import edu.dalhousie.utilities.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompleteCourseList implements ICompleteCourseList {
    private static IDatabaseConnection IDatabaseConnection;

    @Override
    public void getCompleteCourseList() {
        List<Course> courses = new ArrayList<>();
        ICourses coursesList = CourseRegistrationFactory.getInstance().getCourses();

        String query = CourseRegistrationConstants.SELECT_COURSES_QUERY.replace("tableName", Constants.COURSES_TABLE);
        try {
            IDatabaseConnection = DatabaseConnectivity.getInstance();
            final Connection connection = IDatabaseConnection.getDatabaseConnection();
            final Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("course_id"));
                course.setCourseName(resultSet.getString("course_name"));
                course.setAvailableSeats(resultSet.getInt("available_seats"));
                course.setPreRequisiteCourseId(resultSet.getInt("prerequisite_course_id"));
                courses.add(course);
            }
            coursesList.setCourses(courses);
        } catch (Exception exception) {
            ILogger logger = LoggerAbstractFactory.getFactory().newLoggerInstance();
            logger.error(CompleteCourseList.class.toString(), exception.getMessage());
            exception.printStackTrace();
        }
    }
}

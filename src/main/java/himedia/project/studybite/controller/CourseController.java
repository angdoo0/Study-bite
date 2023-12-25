package himedia.project.studybite.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import himedia.project.studybite.domain.Content;
import himedia.project.studybite.domain.ContentData;
import himedia.project.studybite.domain.Course;
import himedia.project.studybite.domain.News;
import himedia.project.studybite.domain.Qna;
import himedia.project.studybite.domain.UserCourse;
import himedia.project.studybite.service.CourseService;
import himedia.project.studybite.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/course")
public class CourseController {
	private final CourseService courseService;
	private final UserCourseService userCourseService;
	
	// 강의 개요
	@GetMapping("/{courseId}")
	public String courseInfo(@PathVariable Long courseId, Model model) {
		Optional<Course> courseInfo = courseService.courseInfo(courseId);

		model.addAttribute("courseInfo", courseInfo.get());
		return "course/info";
	}

	// 강의 목차
	@GetMapping("/{courseId}/contents")
	public String contenList(@PathVariable Long courseId, @SessionAttribute(name = "userId", required = false) Long userId, Model model) {
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		List<Content> contents = courseService.contents(courseId);
		List<UserCourse> userCourses = userCourseService.findUserCourse(userId, courseId);
		Integer attCount = userCourseService.findAttendanceCount(userId, courseId);
		Integer attPercentage = Math.round(((float) attCount / 7F) * 100);

		model.addAttribute("courseInfo", courseInfo.get());
		model.addAttribute("contents", contents);
		model.addAttribute("userCourses", userCourses);
		model.addAttribute("attPercentage", attPercentage);
		return "course/contentList";
	}

	// 강의 콘텐츠 시청
	@GetMapping("/{courseId}/contents/{contentsId}")
	public String content(@PathVariable Long contentsId, @SessionAttribute(name = "userId", required = false) Long userId, Model model) {
		Optional<Content> content = courseService.findContentName(contentsId);
		Optional<ContentData> contentData = courseService.findContentUrl(contentsId);
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		userCourseService.updateDate(date, contentsId, userId);
		
		model.addAttribute("content", content.get());
		model.addAttribute("contentData", contentData.get());
		return "course/content";
	}

	// 강의 공지 목록
	@GetMapping("/{courseId}/news")
	public String news(@PathVariable Long courseId, Model model) {
		List<News> news = courseService.findNewsPage(courseId);
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		
		model.addAttribute("news", news);
		model.addAttribute("courseInfo", courseInfo.get());
		return "/course/news";
	}

	// 강의 공지 상세
	@GetMapping("/{courseId}/news/{newsId}")
	public String newsDesc(@PathVariable Long courseId, @PathVariable Long newsId, Model model) {
		courseService.newsViewCnt(newsId);
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		News news = courseService.findNewsDesc(newsId).get();
		
		model.addAttribute("courseInfo", courseInfo.get());
		model.addAttribute("news", news);
		return "/course/newsDesc";
	}

	// 질의 응답 목록
	@GetMapping("/{courseId}/qna")
	public String qna(@PathVariable Long courseId, Model model) {
		List<Qna> qna = courseService.findQnaPage(courseId);
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		
		model.addAttribute("qna", qna);
		model.addAttribute("courseInfo", courseInfo.get());
		return "/course/qna";
	}

	// 질의 응답 상세
	@GetMapping("/{courseId}/qna/{qnaId}")
	public String qnaDesc(@PathVariable Long courseId, @PathVariable Long qnaId, Model model) {
		courseService.qnaViewCnt(qnaId);
		Qna qna = courseService.findQnaDesc(qnaId).get();
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		
		model.addAttribute("qna", qna);
		model.addAttribute("courseInfo", courseInfo.get());
		return "/course/qnaDesc";
	}

	// 질의 응답 등록 폼
	@GetMapping("/{courseId}/qna/add")
	public String qnaQuestion(@PathVariable Long courseId, Model model) {
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		
		model.addAttribute("courseInfo", courseInfo.get());
		return "/course/qnaForm";
	}

	// 질의 응답 등록
	@PostMapping("/{courseId}/qna/add")
	public String postQnaQuestion(@PathVariable Long courseId, @ModelAttribute Qna qna, Model model) {
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		
		qna.setCourseId(courseId);
		courseService.question(qna);
		
		model.addAttribute("courseInfo", courseInfo.get());
		return "redirect:/course/" + courseId + "/qna/" + qna.getQnaId();
	}
	
	// 출결 확인
	@GetMapping("/{courseId}/attendance")
	public String attendance(@PathVariable Long courseId, @SessionAttribute(name = "userId", required = false) Long userId, Model model) {
		List<UserCourse> userCourses = userCourseService.findUserCourse(userId, courseId);
		Optional<Course> courseInfo = courseService.courseInfo(courseId);
		Integer attCount = userCourseService.findAttendanceCount(userId, courseId);
		Integer attPercentage = Math.round(((float) attCount / 7F) * 100);
		
		model.addAttribute("userCourses", userCourses);
		model.addAttribute("courseInfo", courseInfo.get());
		model.addAttribute("attPercentage", attPercentage);
		return "/course/attendance";
	}
}
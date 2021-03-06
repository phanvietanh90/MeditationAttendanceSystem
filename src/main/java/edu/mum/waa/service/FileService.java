package edu.mum.waa.service;

import edu.mum.waa.entity.AttendanceEntity;
import edu.mum.waa.entity.UserEntity;
import edu.mum.waa.repository.AttendanceRepository;
import edu.mum.waa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserRepository userRepository;

    public boolean processFile(MultipartFile file) {
        if(file == null || file.isEmpty()) return false;
        try {
            String content = new String(file.getBytes(), "UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd/yy");
            LocalDate date;
            Integer userId = 0;
            Long cardId = 0L;
            String type;
            String location;
            AttendanceEntity att = null;
            List<AttendanceEntity> attendanceEntityList = new ArrayList();
            //get list users
            List<UserEntity> userEntities = userRepository.findAll();
            List<Integer> userList = new ArrayList<>();
            for (UserEntity user : userEntities) {
                userList.add(user.getUserid());
                if (user.getCardId() != null) userList.add(user.getCardId().intValue());
            }
            //get list attendances
            List<AttendanceEntity> attendanceEntities = attendanceRepository.findAll();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                //date, user id, name
                if (data.length == 3) {
                     date = LocalDate.parse(data[0], formatter1);
                     userId = Integer.parseInt(data[1]);
                     att = new AttendanceEntity(userId, date, "AM");
                }
                //card id, date, type, location
                else if (data.length == 4) {
                     cardId = Long.parseLong(data[0]);
                     date = LocalDate.parse(data[1], formatter2);
                     type = data[2];
                     location = data[3];
                     att = new AttendanceEntity(cardId, date, type, location);
                }
                //check user existence
                //check attendance existence
                if (att != null
                        && (userList.contains(userId) || userList.contains(cardId.intValue()))
                        && !attendanceEntities.contains(att)
                        && !attendanceEntityList.contains(att))
                    attendanceEntityList.add(att);
            }
            attendanceRepository.saveAll(attendanceEntityList);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

package org.homs.votr;

import org.homs.votr.ent.*;
import org.homs.votr.repo.MessageRepository;
import org.homs.votr.repo.OptionRepository;
import org.homs.votr.repo.UserRepository;
import org.homs.votr.repo.VotrRepository;
import org.homs.votr.util.DateUtil;
import org.homs.votr.util.UUIDUtils;

import java.util.List;

public class VotrService {

    final VotrRepository votrRepository;
    final UserRepository userRepository;
    final OptionRepository optionRepository;
    final MessageRepository messageRepository;

    final UUIDUtils uUIDUtils;
    final DateUtil dateUtil;

    public VotrService(VotrRepository votrRepository, UserRepository userRepository, OptionRepository optionRepository, MessageRepository messageRepository, UUIDUtils uUIDUtils, DateUtil dateUtil) {
        this.votrRepository = votrRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
        this.messageRepository = messageRepository;
        this.uUIDUtils = uUIDUtils;
        this.dateUtil = dateUtil;
    }

    public void createVotr(Votr votr, User creationUser) {

        votr.setVotrHash(uUIDUtils.createUUID(votr.getTitle(), votr.getDescription(), creationUser.getEmail()));
        votr.setCreationDate(dateUtil.now());

        String userHash = uUIDUtils.createUUID(votr.getVotrHash(), creationUser.getEmail());
        votr.setCreationUserHash(userHash);
        creationUser.setUserHash(userHash);

        votrRepository.create(votr);
        creationUser.setVotrId(votr.getVotrId());
        userRepository.save(creationUser);

        createMessage(
                votr.getVotrHash(),
                creationUser.getUserHash(),
                "votr has been created."
        );
    }

    public void createUser(String hashVotr, String emailUser) {

        Votr votr = votrRepository.loadByHash(hashVotr);

        User newUser = new User();
        newUser.setEmail(emailUser);
        newUser.setVotrId(votr.getVotrId());
        newUser.setUserHash(uUIDUtils.createUUID(votr.getVotrHash(), emailUser));
        userRepository.save(newUser);

        createMessage(
                votr.getVotrHash(),
                newUser.getUserHash(),
                String.format("user %s has been created.", newUser.getUserHash())
        );
    }

    public void createOption(String hashVotr, String title, String description) {
        Votr votr = votrRepository.loadByHash(hashVotr);

        OptionId optionId = new OptionId();
        optionId.setVotrId(votr.getVotrId());
        optionId.setNumOrder(optionRepository.getNextNumOrder());

        Option option = new Option();
        option.setOptionId(optionId);
        option.setTitle(title);
        option.setDescription(description);
        optionRepository.save(option);

        createMessage(
                votr.getVotrHash(),
                votr.getCreationUserHash(),
                String.format("option %s has been created.", option.getTitle())
        );
    }

    public void createMessage(String votrHash, String userHash, String message) {
        Votr votr = votrRepository.loadByHash(votrHash);
        User user = userRepository.load(votr.getVotrId(), userHash);
        var m = new Message();
        m.setMessage(message);
        m.setVotrId(votr.getVotrId());
        m.setUserId(user.getUserId());
        m.setTimestamp(dateUtil.now());
        messageRepository.save(m);
    }

    public static class VotrDto {

        public final Votr votr;
        public final User loginedUser;
        public final User creationUser;
        public final List<User> users;
        public final List<Option> options;
        public final List<Message> messages;

        public VotrDto(Votr votr, User loginedUser, User creationUser, List<User> users, List<Option> options, List<Message> messages) {
            this.votr = votr;
            this.loginedUser = loginedUser;
            this.creationUser = creationUser;
            this.users = users;
            this.options = options;
            this.messages = messages;
        }

        @Override
        public String toString() {
            return "VotrDto{" +
                    "votr=" + votr +
                    ", loginedUser=" + loginedUser +
                    ", creationUser=" + creationUser +
                    ", users=" + users +
                    ", options=" + options +
                    ", messages=" + messages +
                    '}';
        }
    }

    public VotrDto loadVotr(String hashVotr, String loginedHashUser) {
        Votr votr = votrRepository.loadByHash(hashVotr);
        User loginedUser = userRepository.load(votr.getVotrId(), loginedHashUser);
        User creationUser = userRepository.load(votr.getVotrId(), votr.getCreationUserHash());
        List<User> users = userRepository.loadByVotrId(votr.getVotrId());
        List<Option> options = optionRepository.load(votr.getVotrId());
        List<Message> messages = messageRepository.load(votr.getVotrId());
        return new VotrDto(votr, loginedUser, creationUser, users, options, messages);
    }
}

package com.ccadmin.app.security.service;

import com.ccadmin.app.person.shared.PersonShared;
import com.ccadmin.app.security.model.entity.AppUserEntity;
import com.ccadmin.app.security.repository.AppProfileRepository;
import com.ccadmin.app.security.repository.AppUserRepository;
import com.ccadmin.app.security.repository.UserProfileRepository;
import com.ccadmin.app.shared.model.dto.ResponsePageSearch;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.dto.SearchDto;
import com.ccadmin.app.shared.service.SearchService;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.user.model.entity.UserStoreEntity;
import com.ccadmin.app.user.repository.UserStoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService extends SessionService {

    private static int limitSearchUser = 10;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppProfileRepository appProfileRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserStoreRepository userStoreRepository;

    @Autowired
    private PersonShared personShared;

    private SearchService searchService;

    public ResponsePageSearch findAll(String Query, int Page)
    {
        SearchDto search = new SearchDto(Query,Page);

        this.searchService = new SearchService(this.appUserRepository);

        ResponsePageSearch responsePage = this.searchService.findAll(search,limitSearchUser);

        for (var user : (List<AppUserEntity>)responsePage.resultSearch)
        {
            user.Person = this.personShared.findById(user.PersonCod);
            user.clearDataSensitive();
        }

        return responsePage;
    }

    public AppUserEntity findById(String UserCod)
    {
        return this.appUserRepository.findById(UserCod).get();
    }

    @Transactional
    public AppUserEntity save(AppUserEntity user)
    {
        UUID uuid = UUID.randomUUID();
        user.addSession(getUserCod(),!this.appUserRepository.existsById(user.UserCod));

        user.Person.PersonType = "01";
        this.personShared.save(user.Person);

        user.Password = new BCryptPasswordEncoder().encode(user.PasswordDecoded);
        user.RecoveryCod = uuid.toString();
        user.Email = user.Person.Email;
        user.PersonCod = user.Person.PersonCod;

        this.appUserRepository.save(user);
        this.userProfileRepository.updateAllStatus("I",user.UserCod);

        for(var item : user.UserProfileList)
        {
            item.addSession(getUserCod(),!this.appUserRepository.existsById(user.UserCod));
            item.UserCod = user.UserCod;
        }
        this.userProfileRepository.saveAll(user.UserProfileList);

        UserStoreEntity userStore = new UserStoreEntity();
        userStore.StoreCod = "T001";
        userStore.UserCod = user.UserCod;
        userStore.CreationUser = "SISTEMA";
        userStore.IsMainStore = "S";
        this.userStoreRepository.save(userStore);

        return user;
    }

    public boolean savePassword(AppUserEntity user)
    {
        AppUserEntity userDB = this.appUserRepository.findById(user.UserCod).get();

        userDB.Password = new BCryptPasswordEncoder().encode(user.Password);

        userDB.addSession(getUserCod(),false);
        this.appUserRepository.save(userDB);

        return true;
    }

    public ResponseWsDto findDataForm(String UserCod)
    {
        ResponseWsDto rpt = new ResponseWsDto();

        Optional<AppUserEntity> UserOptional = this.appUserRepository.findById(UserCod);

        if( UserOptional.isPresent() )
        {
            UserOptional.get().Person = this.personShared.findById(UserOptional.get().PersonCod);
            UserOptional.get().UserProfileList = this.userProfileRepository.findAllByUser(UserOptional.get().UserCod);
            rpt.AddResponseAdditional("User",UserOptional.get());
        }

        rpt.AddResponseAdditional("ProfileList",this.appProfileRepository.findAllActive());

        return rpt;
    }
}

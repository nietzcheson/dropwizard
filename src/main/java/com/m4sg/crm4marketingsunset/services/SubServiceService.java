package com.m4sg.crm4marketingsunset.services;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.m4c.model.entity.ContractService;
import com.m4c.model.entity.SubService;
import com.m4sg.crm4marketingsunset.core.dto.SubServiceDTO;
import com.m4sg.crm4marketingsunset.dao.ContractServiceDAO;
import com.m4sg.crm4marketingsunset.dao.SubServiceDAO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Fernando on 06/04/2015.
 */
public class SubServiceService extends GenericDAOService {

    public SubServiceService() {
        super(SubServiceDAO.class,ContractServiceDAO.class);
    }

    public Optional<SubService> find(long id) {
        return subServiceDAO.find(id);
    }
    public Optional<SubService> findByCampaign(long id) {
        return subServiceDAO.findByCampaign(id);
    }

    public List<SubServiceDTO> find(boolean isCertificateArea, List<Long> callCenterLst) {

        List<SubService> subServiceLst = subServiceDAO.find(isCertificateArea, callCenterLst);

        return Lists.transform(subServiceLst, new Function<SubService, SubServiceDTO>() {
            @Nullable
            @Override
            public SubServiceDTO apply(SubService subService) {
                return new SubServiceDTO(subService.getId(), subService.getName(), subService.getPrice());
            }
        });

    }
    public void register(SubServiceDTO subServiceDTO,Long idBooking){

        Long idServContratado=contractServiceDAO.findMaxByIdBooking(idBooking);
        SubService subService=subServiceDAO.find(subServiceDTO.getIdsubservicio()).get();
        ContractService contractService=new ContractService(idBooking,
                idServContratado,
                subService,
                subServiceDTO.getDescription(),//subServiceOptional.get().getDescription(),
                subServiceDTO.getAudit().getUsername(),null,subServiceDTO.getAdults(),subServiceDTO.getMinor(),
                1,subServiceDTO.getAmount());

//        contractService.setRateExchange(1);
        contractServiceDAO.saveOrUpdate(contractService);
    }
    public ContractService getContractService(Long idBooking,Long idService){
        ContractService contractServiceOptional = contractServiceDAO.find(idBooking,idService);

        return contractServiceOptional;
    }

    public void update( SubServiceDTO subServiceDTO,Long idBooking,Long idContractService){
        ContractService contractService=contractServiceDAO.find(idBooking,idContractService);
        contractService.setAmount(subServiceDTO.getAmount());
        contractService.setPax(subServiceDTO.getAdults());
        contractService.setChilds(subServiceDTO.getMinor());
        contractService.setDescription(subServiceDTO.getDescription());
        SubService subService=subServiceDAO.find(subServiceDTO.getIdsubservicio()).get();
        contractService.setSubService(subService);
        contractServiceDAO.saveOrUpdate(contractService);





    }
    public void delete(Long idBooking,Long idContractService){
        contractServiceDAO.delete(idBooking,idContractService);
    }


}

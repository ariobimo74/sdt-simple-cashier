package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.CompanyProfileDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.OrderDao;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.OrderMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.UserProfileDtoMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.AuthorizeRoleService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.CommonValueService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.UserService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.ServiceUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EGenderType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMemberLevel;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EOrderStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonValueServiceImpl implements CommonValueService {

    private final UserService userService;
    private final OrderDao orderDao;

    private final UserProfileDtoMapper userProfileDtoMapper;

    private final CompanyProfileDao companyProfileDao;

    private final AuthorizeRoleService authorizeRoleService;

    @Override
    public List<ResponseCommonEnumDto> getGenders() {
        return Arrays.stream(EGenderType.values()).map(e -> new ResponseCommonEnumDto(e.name(), e.getTranslatedName()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseCommonEnumDto getDefaultMember(EMemberLevel memberLevel) {
        return Arrays.stream(EMemberLevel.values()).filter(e -> Objects.equals(e, memberLevel)).map(
                e -> new ResponseCommonEnumDto(e.name(), e.name())).findAny().orElse(null);
    }

    @Override
    public List<ResponseCommonEnumDto> getMembers() {
        return Arrays.stream(EMemberLevel.values()).map(
                e -> new ResponseCommonEnumDto(e.name(), e.name())).collect(Collectors.toList());
    }

    @Override
    public List<String> getRecommendedUsername(RequestRecommendedUsernameDto recommendedUsernameDto) {
        List<String> recommendedUsernameList = new ArrayList<>(List.of(
                recommendedUsernameDto.getEmail(),
                recommendedUsernameDto.getIdCardNumber(),
                recommendedUsernameDto.getMobilePhoneNumber()
        )).stream().filter(Objects::nonNull).filter(Strings::isNotBlank).collect(Collectors.toList());
        recommendedUsernameList.addAll(convertNameToRecommended(recommendedUsernameDto.getName(), recommendedUsernameDto.getBirthDate()));
        List<UserReplica> users = userService.getUserByUsernameIn(recommendedUsernameList);

        if (CollectionUtils.isEmpty(users))
            return recommendedUsernameList.stream().distinct().collect(Collectors.toList());
        else return recommendedUsernameList.stream().filter(
                r -> users.stream().noneMatch(u -> Objects.equals(r, u.getUsername()))
        ).distinct().collect(Collectors.toList());
    }

    @Override
    public DashboardIncomeDto getIncomeForDashboard() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> orders = orderDao.findAll(orderDashboardSpecification());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        List<Order> todayOrder = orders.stream().filter(o -> Objects.equals(dateFormat
                .format(o.getCreatedDate()), dateFormat.format(new Date()))).collect(Collectors.toList());

        List<ProductSaleDto> todayProductSale = processProductSales(todayOrder);
        List<ProductSaleDto> monthlyProductSale = processProductSales(orders.stream().filter(o -> o.getCreatedDate().getTime() >= calendar.getTime()
                .getTime()).collect(Collectors.toList()));

        return new DashboardIncomeDto(
                currentUser,
                ServiceUtil.convertPriceWithCurrency(orders.stream().map(Order::getTotalPrice).reduce(Double::sum).orElse(0D)),
                ServiceUtil.convertPriceWithCurrency(todayOrder.stream().map(Order::getTotalPrice).reduce(Double::sum).orElse(0D)),
                ServiceUtil.convertPriceWithCurrency(orders.stream().filter(o -> Objects.equals(dateFormat.format(o.getCreatedDate()), dateFormat.format(new Date()))
                                && Objects.equals(o.getCreatedBy(), currentUser)).map(Order::getTotalPrice).reduce(Double::sum).orElse(0D)),
                todayProductSale, todayProductSale.stream().map(ProductSaleDto::getTotalItem).reduce(Integer::sum).orElse(0),
                processProductSales(orders.stream().filter(o -> o.getCreatedDate().getTime() >= calendar.getTime()
                .getTime()).collect(Collectors.toList())), monthlyProductSale.stream().map(ProductSaleDto::getTotalItem).reduce(Integer::sum).orElse(0)
        );
    }

    private List<String> convertNameToRecommended(String name, Date birthDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> results = new ArrayList<>();
        String[] nameSplit = name.split(" ");
        String[] dateSplit = dateFormat.format(birthDate).split("-");

        if (nameSplit.length > 1)
            results.addAll(List.of(nameSplit[0] + nameSplit[1], nameSplit[0] + "." + nameSplit[1], nameSplit[0] + "_" + nameSplit[1]));
        else results.add(name);
        results.addAll(
                Stream.of(dateSplit).map(d -> Stream.of(nameSplit).map(n -> n + d).collect(Collectors.toList()))
                        .flatMap(Collection::stream).collect(Collectors.toList())
        );

        return results.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    private static Specification<Order> orderDashboardSpecification() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), Date.from(LocalDateTime.now()
                                .minusMonths(6).atZone(ZoneId.systemDefault()).toInstant())),
                        criteriaBuilder.equal(root.get("status"), EOrderStatus.PAID)
                );
    }

    @Override
    public List<CompanyProfileDto> getAllCompanyProfiles() {
        return companyProfileDao.findAllByOrderBySortNumberAsc().stream().map(userProfileDtoMapper::convertCompanyProfile)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyProfileDto patchCompanyProfile(Authentication authentication, RequestEditCompanyProfileDto profileDto) {
        authorizeRoleService.authorizeSuperAdmin(authentication);

        CompanyProfile companyProfile = companyProfileDao.findById(profileDto.getId()).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND
        ));
        companyProfile.setContent(profileDto.getContent());
        companyProfile.setModifiedDate(new Date());
        companyProfile.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return userProfileDtoMapper.convertCompanyProfile(companyProfileDao.save(companyProfile));
    }

    private List<ProductSaleDto> processProductSales(List<Order> orders) {
        Set<Product> productSet = orders.stream().map(Order::getOrderItems).flatMap(Collection::stream).map(OrderItem::getProduct).collect(Collectors.toSet());

        List<ProductSaleDto> productSales = orders.stream().map(Order::getOrderItems).flatMap(Collection::stream)
                .map(o -> o.getProduct().getId()).distinct().map(
                        id -> new ProductSaleDto(id, productSet.stream().filter(
                                p -> Objects.equals(p.getId(), id)).findAny().orElse(new Product()).getName(), 0)
                ).collect(Collectors.toList());
        return productSales.stream().peek(ps -> {
            List<OrderItem> orderById = orders.stream().map(Order::getOrderItems).flatMap(Collection::stream)
                    .filter(t -> Objects.equals(ps.getId(), t.getProduct().getId())).collect(Collectors.toList());
            ps.setTotalItem(orderById.stream().map(OrderItem::getTotalItem).reduce(Integer::sum).orElse(0));
        }).sorted(Comparator.comparing(ProductSaleDto::getTotalItem).reversed()).collect(Collectors.toList());
    }
}

package id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dao.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.OrderMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.mapper.ProductMapper;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.service.OrderService;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.GoogleMailSenderUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.common.PageableDataTableCommonUtil;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.DataNotFoundException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.InvalidDataException;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.exception.UploadImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static id.co.softwaredeveloperstoday.cms.dashboard.web.service.impl.ProductServiceImpl.specificationProductDataTable;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final ProductDao productDao;
    private final OrderDao orderDao;

    private final UserDao userDao;
    private final UserProfileDao userProfileDao;
    private final CompanyProfileDao companyProfileDao;

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    private final TemplateEngine templateEngine;

    @Override
    public ResponseDataTableDto<ProductOrderDataTableDto> getAllPagingDataTable(int draw, String search, Integer page, Integer size) {
        Page<Product> products;
        PageRequest pageRequest = PageRequest.of(
                Optional.ofNullable(page).orElse(IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_NUMBER) -
                        IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_NUMBER,
                Optional.ofNullable(size).orElse(IApplicationConstant.CommonValue.Pagination.DEFAULT_PAGE_SIZE),
                Sort.by(EProductDataTableSortBy.NAME.getName()).ascending()
        );

        if (StringUtils.isBlank(search))
            products = productDao.findAll(pageRequest);
        else products = productDao.findAll(specificationProductDataTable(search), pageRequest);

        if (products.getTotalElements() == 0)
            return new ResponseDataTableDto<>(draw, 0, 0, new ArrayList<>());
        else return new ResponseDataTableDto<>(draw, productDao.count(), products.getTotalElements(), products.getContent()
                .stream().map(productMapper::convertProductOrderDataTableDto).peek(p -> {
                    p.setActionsId(new ActionDataTableIdentityDto(p.getId(), List.of(
                            new ActionDataTableDto("btn btn-success btn-add-to-cart", "Add Product Order", "&#xf217")
                    )));
                }).collect(Collectors.toList()));
    }

    @Override
    public OrderDetailDto getOrderById(Long id) {
        Order order = orderDao.findById(id).orElseThrow(() -> new DataNotFoundException(
                IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );
        OrderDetailDto detailDto = orderMapper.convertOrderDetailDto(order);
        detailDto.setTotalOrderedItem(
                order.getOrderItems().stream().map(OrderItem::getTotalItem).reduce(Integer::sum).orElse(0)
        );

        return detailDto;
    }

    @Override
    public CalculateOrderDto calculateOrder(List<RequestCalculateOrderDto> requestCalculateOrderDtoList) {
        return calculateOrder(requestCalculateOrderDtoList, productDao.findByIdIn(requestCalculateOrderDtoList.stream()
                .filter(r -> r.getTotalItem() > 0)
                .map(RequestCalculateOrderDto::getProductId).collect(Collectors.toList())));
    }

    @Override
    public OrderDetailDto checkout(List<RequestCalculateOrderDto> requestCalculateOrderDtoList) {
        final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmsss");
        Date date = new Date();

        Optional<CompanyProfile> companyProfile = companyProfileDao.findFirstByName(ECompanyProfileLabel.COMPANY_LABEL.name());
        String defaultOrderLabel = IApplicationConstant.CommonValue.DEFAULT_INITIALIZATION_FOR_ORDER_NUMBER;

        if (companyProfile.isPresent())
            defaultOrderLabel = companyProfile.get().getContent().split(" ")[0].toUpperCase();

        String orderNumber = defaultOrderLabel + format.format(date);
        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Product> products = productDao.findByIdIn(requestCalculateOrderDtoList.stream()
                .filter(r -> r.getTotalItem() > 0)
                .map(RequestCalculateOrderDto::getProductId).collect(Collectors.toList()));
        CalculateOrderDto orderDto = calculateOrder(requestCalculateOrderDtoList, products);

        products.forEach(p -> {
            RequestCalculateOrderDto requestCalculateOrderDto = requestCalculateOrderDtoList.stream().filter(
                    r -> Objects.equals(p.getId(), r.getProductId())).findAny().orElseThrow(
                            () -> new DataNotFoundException(
                                    IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND_CHECKOUT
                            )
            );
            if (Objects.nonNull(requestCalculateOrderDto) && p.getStock() > 0) {
                long result = p.getStock() - requestCalculateOrderDto.getTotalItem().longValue();
                if (result >= 0) p.setStock(result);
                else p.setStock(0L);
            }
        });
        productDao.saveAll(products);

        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setCashPaidByCustomer(0D);
        order.setStatus(EOrderStatus.UNPAID);
        order.setCreatedBy(createdBy);
        order.setCreatedDate(date);
        order.setIsDelete(false);

        List<OrderItem> orderItems = new ArrayList<>();
        orderDto.getOrderItemDtoList().forEach(oi -> {
            OrderItem orderItem = orderMapper.convertOrderItem(oi);
            orderItem.setOrder(order);
            orderItem.setCreatedDate(date);
            orderItem.setCreatedBy(createdBy);
            orderItem.setIsDelete(false);
            orderItems.add(orderItem);
        });
        order.setOrderItems(orderItems);

        return orderMapper.convertOrderDetailDto(orderDao.save(order));
    }

    @Override
    public OrderDetailDto payment(RequestPaymentDto requestPaymentDto, MultipartFile image) {
        Order order = orderDao.findById(requestPaymentDto.getOrderId()).orElseThrow(
                () -> new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND)
        );

        if (Objects.equals(EOrderStatus.UNPAID, order.getStatus())) {
            if (requestPaymentDto.getPaidByCustomer() < order.getTotalPrice())
                throw new InvalidDataException(
                        IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_TRANSACTION_ORDER_AMOUNT_PAYMENT_TOTAL_PRICE
                );

            order.setCashPaidByCustomer(requestPaymentDto.getPaidByCustomer());
            order.setPaymentMethod(requestPaymentDto.getPaymentMethod());
            order.setChange(requestPaymentDto.getPaidByCustomer() - order.getTotalPrice());
            order.setStatus(EOrderStatus.PAID);
        }

        if (Objects.nonNull(image)) {
            try {
                order.setAttachment(image.getBytes());
            } catch (IOException e) {
                throw new UploadImageException();
            }
        } else order.setAttachment(requestPaymentDto.getAttachment());
        order.setNotes(requestPaymentDto.getNotes());

        order.setModifiedDate(new Date());
        order.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        if (Objects.equals(requestPaymentDto.getSendMail(), Boolean.TRUE)
                && StringUtils.isNotBlank(requestPaymentDto.getEmailAddress())) {
            try {
                List<CompanyProfile> companyProfiles = companyProfileDao.findAllByOrderBySortNumberAsc();

                GoogleMailSenderUtil senderUtil = new GoogleMailSenderUtil(
                        companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                                ECompanyProfileLabel.EMAIL_SUBJECT.name())).findAny().orElse(new CompanyProfile()).getContent(),
                        generateAndDownloadPdf(order.getId(), EPdfTemplatePath.MAIL, companyProfiles).getProcessedTemplateEngine(),
                        companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                                ECompanyProfileLabel.COMPANY_EMAIL_ADDRESS.name())).findAny().orElse(new CompanyProfile()).getContent(),
                        companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                                ECompanyProfileLabel.COMPANY_EMAIL_SECRET.name())).findAny().orElse(new CompanyProfile()).getContent(),
                        generateAndDownloadPdf(order.getId(), EPdfTemplatePath.PRINT, companyProfiles).getPdfFile()

                );
                senderUtil.sendEmail(requestPaymentDto.getEmailAddress());
            } catch (IOException e) {
                log.error("failed to send mail with error : " + e.getMessage());
                log.error("continue payment process");
            }
        }

        return orderMapper.convertOrderDetailDto(orderDao.save(order));
    }

    @Override
    public ResponseDataTableDto<TransactionHistoryDataTableDto> getAllTransactionHistoryDataTable(
            int draw, String search, Integer page, Integer size, ETransactionHistorySortBy sortBy, boolean isAscendingSort)
    {
        Page<Order> orders;
        if (StringUtils.isBlank(search))
            orders = orderDao.findAll(PageableDataTableCommonUtil.pageable(page, size, sortBy.getName(), isAscendingSort));
        else orders = orderDao.findAll(
                orderSpecificationDataTable(search), PageableDataTableCommonUtil.pageable(page, size, sortBy.getName(), isAscendingSort)
        );

        if (orders.getTotalElements() == 0)
            return new ResponseDataTableDto<>(draw, 0, 0, new ArrayList<>());
        else return new ResponseDataTableDto<>(draw, orderDao.count(), orders.getTotalElements(), orders.getContent()
                .stream().map(orderMapper::convertTransactionHistoryDataTableDto).peek(
                        o -> o.setActionsId(new ActionDataTableIdentityDto(o.getId(), transactionHistoryAction()))
                ).collect(Collectors.toList()));
    }

    @Override
    public ProcessedGeneratedPdfDto generateAndDownloadPdf(Long orderId, EPdfTemplatePath path) throws IOException {
        return generateAndDownloadPdf(orderId, path, companyProfileDao.findAllByOrderBySortNumberAsc());
    }

    private ProcessedGeneratedPdfDto generateAndDownloadPdf(Long orderId, EPdfTemplatePath path, List<CompanyProfile> companyProfiles) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OrderDetailDto detailDto = getOrderById(orderId);
        String cashierName = detailDto.getCreatedBy();

        User user = userDao.findByUsernameIgnoreCase(cashierName);
        if (Objects.nonNull(user) && Objects.nonNull(user.getId())) {
            UserProfile profile = userProfileDao.findByUserId(user.getId());

            if (Objects.nonNull(profile) && Objects.nonNull(profile.getName()))
                cashierName = profile.getName();
        }

        Context context = new Context();
        context.setVariable("orderNumber", detailDto.getOrderNumber());
        context.setVariable("date", dateFormat.format(detailDto.getCreatedDate()));
        context.setVariable("cashierName", cashierName);
        context.setVariable("paymentMethod", detailDto.getPaymentMethod());

        context.setVariable("items", detailDto.getOrderItemDtoList());

        context.setVariable("totalAllPrice", detailDto.getTotalPriceWithCurrency());
        context.setVariable("paidByCustomer", detailDto.getCashPaidByCustomerWithCurrency());
        context.setVariable("change", detailDto.getChangeWithCurrency());

        context.setVariable("shopName", companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                ECompanyProfileLabel.COMPANY_NAME.name())).findAny().orElse(new CompanyProfile()).getContent());
        context.setVariable("shopAddress", companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                ECompanyProfileLabel.COMPANY_ADDRESS.name())).findAny().orElse(new CompanyProfile()).getContent());
        context.setVariable("shopLabel", companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                ECompanyProfileLabel.COMPANY_LABEL.name())).findAny().orElse(new CompanyProfile()).getContent());
        context.setVariable("shopPhoneNumber",
                companyProfiles.stream().filter(c -> Objects.equals(c.getName(),
                        ECompanyProfileLabel.PHONE_NUMBER.name())).findAny().orElse(new CompanyProfile()).getContent());

        String processedTemplateEngine = templateEngine.process(path.getName(), context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(processedTemplateEngine, null);
        builder.toStream(outputStream);
        builder.run();

        return new ProcessedGeneratedPdfDto(detailDto.getOrderNumber(), new Date().getTime(), outputStream.toByteArray(),
                processedTemplateEngine);
    }

    private CalculateOrderDto calculateOrder(List<RequestCalculateOrderDto> requestCalculateOrderDtoList, List<Product> products) {
        List<CalculateOrderItemDto> orderedItems = products.stream().map(productMapper::convertCalculateOrderItemDto)
                .collect(Collectors.toList());

        if (orderedItems.isEmpty())
            throw new DataNotFoundException(IApplicationConstant.CommonMessage.ErrorMessage.ERROR_MESSAGE_DATA_NOT_FOUND_CALCULATE);

        double totalPrice = 0;
        int totalAllItems = 0;

        for (CalculateOrderItemDto itemDto : orderedItems) {
            itemDto.setTotalItem(requestCalculateOrderDtoList.stream().filter(
                    r -> Objects.equals(r.getProductId(), itemDto.getProductId())
            ).map(RequestCalculateOrderDto::getTotalItem).findAny().orElse(0));
            totalAllItems += itemDto.getTotalItem();

            double totalCurrentItemPrice = itemDto.getCurrentPricePerItemUnit() * itemDto.getTotalItem();
            itemDto.setSumTotalAllItemPrice(totalCurrentItemPrice);
            itemDto.setSumTotalAllItemPriceWithCurrency(ProductMapper.convertPriceWithCurrency(totalCurrentItemPrice));
            totalPrice += totalCurrentItemPrice;
        }

        return new CalculateOrderDto(totalPrice, totalAllItems, orderedItems, ProductMapper.convertPriceWithCurrency(totalPrice));
    }

    private static Specification<Order> orderSpecificationDataTable(String search) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("createdDate"), criteriaBuilder.literal("YYYY-MM-DD")), '%' + search + '%'),
                        criteriaBuilder.like(criteriaBuilder.function("TO_CHAR", String.class, root.get("modifiedDate"), criteriaBuilder.literal("YYYY-MM-DD")), '%' + search + '%')
                ),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("paymentMethod").as(String.class)), '%' + search.toLowerCase() + '%'),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), '%' + search.toLowerCase() + '%'),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("orderNumber")), '%' + search.toLowerCase() + '%')
        );
    }

    private List<ActionDataTableDto> transactionHistoryAction() {
        return List.of(
                new ActionDataTableDto(EDataTableAction.DETAIL), new ActionDataTableDto(EDataTableAction.UPDATE),
                new ActionDataTableDto("btn btn-info download-item-btn", "Download PDF", "&#xf019")
        );
    }

}

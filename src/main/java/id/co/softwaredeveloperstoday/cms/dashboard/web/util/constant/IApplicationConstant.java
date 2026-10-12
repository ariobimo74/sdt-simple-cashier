package id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant;

public interface IApplicationConstant {

    interface RestVersion {
        String API = "/api";

        interface Menu {
            String MENU_V1 = API + "/v1/menu";
            String MENU_V2 = API + "/v2/menu";
        }

        interface Register {
            String REGISTER_V1 = API + "/v1/register";
        }

        interface Role {
            String ROLE_V1 = API + "/v1/role";
        }

        interface CommonValue {
            String COMMON_VALUE = API + "/v1/value";
            String GENDER = "/gender";
            String MEMBER = "/member";
            String MEMBER_DEFAULT = "/member-default";
            String RECOMMENDED_USERNAME = "/recommended-username";

            String ADDITIONAL_PAGINATION_PARAMETERS = "/additional-pagination-parameters";

            String DASHBOARD_INCOME = "/dashboard";

            String COMPANY_PROFILE = "/company-profile";
        }

        interface User {
            String USER_V1 = API + "/v1/user";

            String VIEW_USER_PROFILES = "/profiles";
            String VIEW_USER_PROFILES_DATA_TABLE = "/profiles-data-table";
            String VIEW_USER_PROFILE = "/profile/{id}";
            String EDIT_USER_PROFILE = "/edit/{id}";

            String CHANGE_PASSWORD = "/change-password";
            String CURRENT_ROLE = "/current-role";
            String CHANGE_PASSWORD_ALLOWED_USER = "/allowed-user-to-change-password";
            String CREATE_EDIT_ACTION_ALLOWED_USER = "/allowed-user-to-create-edit-user";
        }

        interface Product {
            String PRODUCT_V1 = API + "/v1/product";

            String VIEW_CATEGORIES = "/categories";

            String VIEW_ITEM_ID = "/item/{id}";

            String UPDATE_ITEM_ID = "/product/update/{id}";
            String DETAIL_ITEM_ID = "/product/detail/{id}";
            String VIEW_ITEM_CODE = "/code/{code}";

            String ADD = "/product/add";
        }

        interface Category {
            String CATEGORY_V1 = API + "/v1/category";

            String ITEM_ID = "/{id}";

        }

        interface Order {
            String ORDER_V1 = API + "/v1/order";

            String CALCULATE_ORDER = "/calculate";

            String PRODUCT = "/order";
            String HISTORY = "/history";

            String DETAIL_ID = "/detail/{id}";
            String PAYMENT_ID = PRODUCT + "/payment/{id}";
            String PAYMENT = "/payment";

            String CHECKOUT = "/checkout";

            String DOWNLOAD_PDF_ID = "/download-pdf/{id}";
        }
    }

    interface SchemaName {
        String MASTER_AUTHENTICATION = "master_authentication";
        String MASTER_CATALOG = "master_catalog";
        String TRANSACTION = "transaction";
    }

    interface CommonMessage {
        interface ErrorMessage {
            String ERROR_MESSAGE_USER = "Invalid Username and Password";
            String ERROR_MESSAGE_USER_NOT_ALLOWED = "User is not allowed to change other users' password";
            String ERROR_OLD_PASSWORD_NOT_MATCH = "Old password is invalid";
            String ERROR_NEW_PASSWORD_NOT_MATCH = "New password is not match";

            String ERROR_MESSAGE_USER_FOUND_USERNAME = "User not found according to username";
            String ERROR_MESSAGE_USER_NOT_FOUND = "User not found";

            String ERROR_MESSAGE_DATA_NOT_FOUND = "No Data found";

            String ERROR_MESSAGE_DATA_NOT_FOUND_CHECKOUT = "No Data found while checkout item/product. Please check each of product availability";
            String ERROR_MESSAGE_DATA_NOT_FOUND_CALCULATE = "Please select data for checkout!";

            String ERROR_MESSAGE_GENERAL_REQUIRED_FIELDS = " Required Parameter(s) should Be Filled: There is/are field(s)/parameter(s) that is/are empty/blank. Please insert Required Field(s) ";

            String ERROR_MESSAGE_UNAUTHORIZED = "Unauthorized";

            String ERROR_MESSAGE_STRONG_PASSWORD = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit, and one special character";

            String ERROR_MESSAGE_TRANSACTION_ORDER_AMOUNT_PAYMENT_TOTAL_PRICE = "Paid by customer cannot be lower than total price";

            String ERROR_MESSAGE_TRANSACTION_ORDER_UPLOADED_ATTACHMENT_MAX_SIZE = "Uploaded file exceeded maximum size. Please resize your file size";
        }
    }

    interface CommonValue {
        String DEFAULT_INITIALIZATION_FOR_ORDER_NUMBER = "AZNA";

        interface DefaultEntityWhereClause {
            String ENTITY_WHERE_CLAUSE = " is_delete = false ";
        }

        interface RestResponseValue {
            String SUCCESS = "SUCCESS";
            String FAILED = "FAILED";
        }

        interface CommonRestParam {
            String FIND_BY_NAME = "name";
            String FIND_BY_USER_NAME = "username";
            String FIND_BY_DATE_OF_BIRTH = "date-of-birth";

            String PAGE = "page";
            String SIZE = "size";

            String PARAM_STRONG_PASSWORD = "StrongPassword";
        }

        interface CommonRestPath {
            String NAME = "name";
            String ID = "id";
            String CODE = "code";

            String FIND_BY_NAME = "find-by-name";
        }

        interface TimeZone {
            String DEFAULT = "Asia/Jakarta";
        }

        interface Pagination {
            Integer DEFAULT_PAGE_NUMBER = 1;
            Integer DEFAULT_PAGE_SIZE = 10;

            String DEFAULT_SORT_BY = "id";

            String SORT_TYPE_PARAMETER = "sort-type";
            String SORT_BY_PARAMETER = "sort-by";
        }
    }

}

package ru.zhenyria.restaurants.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zhenyria.restaurants.util.ValidationUtil;
import ru.zhenyria.restaurants.util.exception.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static ru.zhenyria.restaurants.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    public static final String EXCEPTION_DUPLICATE_EMAIL = "error.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_NAME = "error.duplicateName";
    public static final String EXCEPTION_DUPLICATE_NAME_PRICE = "error.duplicateNameAndPrice";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = Map.of(
            "user_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurants_name_idx", EXCEPTION_DUPLICATE_NAME,
            "dishes_name_price_idx", EXCEPTION_DUPLICATE_NAME_PRICE);

    private final MessageSourceAccessor messageSourceAccessor;

    public ExceptionInfoHandler(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(VotingException.class)
    public ResponseEntity<ErrorInfo> votingError(HttpServletRequest req, VotingException e) {
        return logAndGetErrorInfo(req, e, false, VOTING_ERROR);
    }

    @ExceptionHandler({UnsupportedOperationException.class, NotAvailableOperationException.class})
    public ResponseEntity<ErrorInfo> forbiddenError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, FORBIDDEN_OPERATION);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_I18N_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, WRONG_DATA, messageSourceAccessor.getMessage(entry.getValue()));
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, WRONG_DATA);
    }

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class, WrongDataException.class})
    public ResponseEntity<ErrorInfo> dataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, WRONG_DATA);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorInfo> transactionError(HttpServletRequest req, TransactionSystemException e) {
        return logAndGetErrorInfo(req, e, false, OPERATION_FAILED);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, WRONG_DATA, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);
        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}

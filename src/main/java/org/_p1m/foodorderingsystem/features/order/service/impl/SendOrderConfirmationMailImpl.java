package org._p1m.foodorderingsystem.features.order.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org._p1m.foodorderingsystem.common.util.ServerUtil;
import org._p1m.foodorderingsystem.features.order.repository.OrderDataRepository;
import org._p1m.foodorderingsystem.features.order.service.SendOrderConfirmationMail;
import org._p1m.foodorderingsystem.model.AddCartData;
import org._p1m.foodorderingsystem.model.Address;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.Menu;
import org._p1m.foodorderingsystem.model.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendOrderConfirmationMailImpl implements SendOrderConfirmationMail {

    private final OrderDataRepository orderRepository;
    private final JavaMailSender mailSender;
    private final ServerUtil serverUtil;

    @Autowired
    public SendOrderConfirmationMailImpl(OrderDataRepository orderRepository, JavaMailSender mailSender, ServerUtil serverUtil) {
        this.orderRepository = orderRepository;
        this.mailSender = mailSender;
        this.serverUtil = serverUtil;
    }

    @Override
    public void sendEmail(Long orderId) {
        try {
            processOrderData(orderId);
        } catch (Exception e) {
            log.error("Error while sending email for orderId {}: {}", orderId, e.getMessage(), e);
        }
    }

    @Transactional
    private void processOrderData(Long orderId) throws MessagingException, IOException {
        OrderData order = orderRepository.findById(orderId).orElseThrow();
        AddCartData cart = order.getAddCartData();
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        currencyFormat.setMaximumFractionDigits(0);
        currencyFormat.setMinimumFractionDigits(0);

        String username = cart.getCustomer().getProfile().getName();
        Address address = order.getUserAddress();

        String userAddress = Stream.of(
                address.getRegion(),
                address.getCity(),
                address.getTownship(),
                address.getRoad(),
                address.getStreet()
            )
            .filter(Objects::nonNull)
            .filter(s -> !s.isBlank())
            .collect(Collectors.joining(", "));

        String userEmail = cart.getCustomer().getEmail();
        Integer quantity = cart.getQuantity();
        String formattedOrderId = "ORDER_00" + orderId;
        String orderTime = order.getOrderDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Menu menu = cart.getDishSize().getMenu();
        String menuName = menu.getDish();
        BigDecimal menuPrice = menu.getPrice().multiply(BigDecimal.valueOf(quantity));
        String restaurantName = menu.getRestaurant().getRestaurantName();
        String dishSize = cart.getDishSize().getName();

        Extra extra = cart.getExtra();
        String extraName = (extra != null) ? extra.getName() : "";
        BigDecimal extraPrice = (extra != null) ? extra.getPrice() : BigDecimal.ZERO;

        BigDecimal totalPrice = menuPrice.add(extraPrice);

        StringBuilder menuListBuilder = new StringBuilder();
        menuListBuilder.append("<tr>")
                .append("<td colspan=\"2\" style=\"padding: 10px 0;\">")
                .append("<table width=\"100%\" style=\"font-size: 14px;\">")
                .append("<tr>")
                .append("<td style=\"width: 70%; vertical-align: top;\"><strong>")
                .append(quantity).append(" X ").append(menuName)
                .append("</strong></td>")
                .append("<td style=\"width: 30%; text-align: right; vertical-align: top;\">")
                .append("$ ").append(currencyFormat.format(menuPrice))
                .append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td colspan=\"2\" style=\"color: #555; font-size: 13px; padding-top: 4px;\">")
                .append("Size: ").append(dishSize).append("<br>");

        if (!extraName.isBlank()) {
            menuListBuilder.append("Extras: ").append(extraName)
                    .append(" (+$").append(currencyFormat.format(extraPrice)).append(")");
        }

        menuListBuilder.append("</td></tr>")
                .append("</table>")
                .append("</td>")
                .append("</tr>");


        String htmlContent = serverUtil.loadTemplate("templates/mailTemplates/orderConfirmMail.html");

        htmlContent = htmlContent
                .replace("${username}", username)
                .replace("${address}", userAddress)
                .replace("${orderId}", formattedOrderId)
                .replace("${orderTime}", orderTime)
                .replace("${greetingName}", username)
                .replace("${restaurantName}", restaurantName)
                .replace("${menuList}", menuListBuilder.toString())
                .replace("${subTotal}", "$ " + currencyFormat.format(totalPrice))
                .replace("${orderTotal}", "$ " + currencyFormat.format(totalPrice));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(userEmail);
        helper.setSubject("Your Order has been placed");
        helper.setText(htmlContent, true);
        helper.addInline("logoImg", new ClassPathResource("templates/logo/logo.png"));

        mailSender.send(message);
    }
}

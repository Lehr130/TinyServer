package tiny.lehr.tomcat.bean;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Lehr
 * @create: 2020-02-18
 */
public class TommyResponseFacade implements HttpServletResponse {

    HttpServletResponse res;

    public TommyResponseFacade(HttpServletResponse res)
    {
        this.res = res;
    }

    @Override
    public void addCookie(Cookie cookie) {
        res.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String s) {
        return res.containsHeader(s);
    }

    @Override
    public String encodeURL(String s) {
        return res.encodeURL(s);
    }

    @Override
    public String encodeRedirectURL(String s) {
        return res.encodeRedirectURL(s);
    }

    @Override
    public String encodeUrl(String s) {
        return res.encodeURL(s);
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return res.encodeRedirectUrl(s);
    }

    @Override
    public void sendError(int i, String s) throws IOException {
        res.sendError(i,s);
    }

    @Override
    public void sendError(int i) throws IOException {
        res.sendError(i);
    }

    @Override
    public void sendRedirect(String s) throws IOException {
        res.sendRedirect(s);
    }

    @Override
    public void setDateHeader(String s, long l) {
        res.setDateHeader(s,l);
    }

    @Override
    public void addDateHeader(String s, long l) {
        res.addDateHeader(s,l);
    }

    @Override
    public void setHeader(String s, String s1) {
        res.setHeader(s,s1);
    }

    @Override
    public void addHeader(String s, String s1) {
        res.addHeader(s,s1);
    }

    @Override
    public void setIntHeader(String s, int i) {
        res.setIntHeader(s,i);
    }

    @Override
    public void addIntHeader(String s, int i) {
        res.addIntHeader(s,i);
    }

    @Override
    public void setStatus(int i) {
        res.setStatus(i);
    }

    @Override
    public void setStatus(int i, String s) {
        res.setStatus(i,s);
    }

    @Override
    public int getStatus() {
        return res.getStatus();
    }

    @Override
    public String getHeader(String s) {
        return res.getHeader(s);
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return res.getHeaders(s);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return res.getHeaderNames();
    }

    @Override
    public String getCharacterEncoding() {
        return res.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return res.getContentType();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return res.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return res.getWriter();
    }

    @Override
    public void setCharacterEncoding(String s) {
        res.setCharacterEncoding(s);
    }

    @Override
    public void setContentLength(int i) {
        res.setContentLength(i);
    }

    @Override
    public void setContentLengthLong(long l) {
        res.setContentLengthLong(l);
    }

    @Override
    public void setContentType(String s) {
        res.setContentType(s);
    }

    @Override
    public void setBufferSize(int i) {
        res.setBufferSize(i);
    }

    @Override
    public int getBufferSize() {
        return res.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
        res.flushBuffer();
    }

    @Override
    public void resetBuffer() {
        res.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
        return res.isCommitted();
    }

    @Override
    public void reset() {
        res.reset();
    }

    @Override
    public void setLocale(Locale locale) {
        res.setLocale(locale);
    }

    @Override
    public Locale getLocale() {
        return res.getLocale();
    }
}

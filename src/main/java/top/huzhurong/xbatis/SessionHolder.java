package top.huzhurong.xbatis;

import org.apache.ibatis.session.SqlSession;

/**
 * @author luobo.cs@raycloud.com
 * @since 2018/10/7
 */
public class SessionHolder {

    public static ThreadLocal<SessionHolder> holder = new ThreadLocal<>();
    private SqlSession sqlSession;

    public void remove() {
        holder.remove();
    }

    public void setHolder(SessionHolder holder) {
        holder.setHolder(holder);
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
}

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="image/jpeg" import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*,com.sun.image.codec.jpeg.*,java.io.OutputStream"%>
<%!
	Color getRandColor(int fc, int bc) {//给定范围获得随机颜色
	Random random = new Random();
	if (fc > 255)
		fc = 255;
	if (bc > 255)
		bc = 255;
	int r = fc + random.nextInt(bc - fc);
	int g = fc + random.nextInt(bc - fc);
	int b = fc + random.nextInt(bc - fc);
	return new Color(r, g, b);
	}
%>
<%
try {
	//设置页面不缓存
	response.setContentType("image/jpeg");
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);

	//生成随机验证码
	Random random = new Random();
	String sRand = "";
	for (int i = 0; i < 5; i++) {
		String rand = String.valueOf(random.nextInt(10));
		sRand += rand;
	}
	
	// 将认证码存入SESSION
	session.setAttribute("SESSION_VALIDATE_CODE", sRand);

	// 在内存中创建图象
	int width = 75;
	int height = 30;
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	// 获取图形上下文
	Graphics g = image.getGraphics();

	// 设定背景色
	g.setColor(getRandColor(240, 255));
	g.fillRect(0, 0, width, height);

	//设定字体
	g.setFont(new Font("serif", Font.CENTER_BASELINE, 24));

	//画边框
	g.setColor(new Color(1));
	g.drawRect(0,0,width-1,height-1);

	// 随机产生干扰线，使图象中的认证码不易被其它程序探测到
	g.setColor(getRandColor(100, 150));
	for (int i = 0; i < 100; i++) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(12);
		int yl = random.nextInt(12);
		g.drawLine(x, y, x + xl, y + yl);
	}

	for (int i = 0; i < 5; i++) {
		String rand = sRand.substring(i, i + 1);
		// 将认证码显示到图象中
		g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
		//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
		g.drawString(rand, 13 * i + 6, 23);
	}

	// 图象生效
	g.dispose();
	response.reset();

	OutputStream os = response.getOutputStream();
	// 输出图象到页面
	ImageIO.write(image, "JPEG", os);
	
	os.flush();
	os.close();
	response.flushBuffer();  
	out.clear();  
	out = pageContext.pushBody(); 
	
} catch(IllegalStateException e) {
	System.out.println(e.getMessage());  
	e.printStackTrace();
} catch(Exception e) {
	
}
%>

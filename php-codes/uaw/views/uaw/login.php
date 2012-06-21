<h2>Login</h2>
<?php echo validation_errors(); ?>
<?php if($mailmesg) {echo $mailmesg;} ?>
<?php echo form_open('login/verifylogin') ?>
	<label for="email">Email</label> 
	<input type="input" name="email" /><br />
	<label for="password">Password</label>
<input type="password" name="password" /><br />	
	<input type="submit" name="submit" value="Login" /> 
</form>
	<?php echo anchor('login/register', 'Register', 'title="Register"');?>

<h2>Login</h2>
<?php echo validation_errors(); ?>

<?php echo form_open('login') ?>
	<label for="username">Username</label> 
	<input type="input" name="username" /><br />
	<label for="password">Password</label>
<input type="password" name="password" /><br />	
	<input type="submit" name="submit" value="Login" /> 
</form>
	<?php echo anchor('login/register', 'Register', 'title="Register"');?>
